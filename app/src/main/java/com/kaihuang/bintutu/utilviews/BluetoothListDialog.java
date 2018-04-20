package com.kaihuang.bintutu.utilviews;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.utils.PermissionsChecker;
import com.kaihuang.bintutu.utilviews.task.BlueConnectTask;
import com.kaihuang.bintutu.utilviews.util.BluetoothUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static android.content.ContentValues.TAG;


/**
 * Created by Administrator on 2018/3/31.
 */

public class BluetoothListDialog extends Dialog {
    @Bind(R.id.recycler_bluetooth)
    RecyclerView recycler_bluetooth;

    private View view;
    private Context mcontext;
    private BluetoothReceiver blueReceiver;
    private List<BlueDeviceBean> lstDevices = new ArrayList<BlueDeviceBean>();
    private BluetoothAdapter mBluetooth;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_WIFI_STATE"};
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private BlueListAdapter listAdapter;
    private BluetoothSocket mBlueSocket;
    //收到对方发来的消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.e("readMessage = ",readMessage);
            }
        }
    };
    public BluetoothListDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mcontext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.bluetooth_list_dailog, null);
        ButterKnife.bind(this, view);
        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (mBluetooth == null) {
            Toast.makeText(mcontext, "该设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            return;
        }
        setView();
        initData();
        if (!mBluetooth.isDiscovering()) {
            lstDevices.clear();
            mBluetooth.startDiscovery();
        }
    }

    /**
     * 初始化数据界面
     */
    private void initData() {
        listAdapter = new BlueListAdapter(mcontext, lstDevices);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getInstance());
        recycler_bluetooth.setLayoutManager(linearLayoutManager);
        recycler_bluetooth.setAdapter(listAdapter);
        listAdapter.setOnItemClickLitener(new BlueListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                cancelDiscovery();
                BlueDeviceBean item = lstDevices.get(position);
                BluetoothDevice device = mBluetooth.getRemoteDevice(item.getAddress());
                try {
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
//                        Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
//                        Log.e("hha", "开始配对");
//                        Boolean result = (Boolean) createBondMethod.invoke(device);
                        Log.e("ywq", "attemp to bond:"+"["+device.getName()+"]");
//                        try {
//                            //通过工具类ClsUtils,调用createBond方法
//                            ClsUtils.createBond(device.getClass(), device);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        BluetoothGatt mBluetoothGatt =  device.connectGatt(mcontext, false, mGattCallback);
                        if(mBluetoothGatt.connect()){
                            Log.e("hha", "连接成功");
                        }else{
                            Log.e("hha", "连接失败");
                        }
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDED && item.getBondState() != 3) {
                        Log.e("hha", "开始连接");
                        BlueConnectTask connectTask = new BlueConnectTask(item.getAddress());
                        connectTask.setBlueConnectListener(new BlueConnectTask.BlueConnectListener() {
                            @Override
                            public void onBlueConnect(String address, BluetoothSocket socket) {
                                Log.e("hha", "连接成功");
                                mBlueSocket = socket;
                                refreshAddress(address);
                            }
                        });
                        connectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, device);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("hha", "配对异常");
                }
            }
        });

        blueReceiver = new BluetoothReceiver();
        //需要过滤多个动作，则调用IntentFilter对象的addAction添加新动作
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        foundFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        foundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        foundFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        mcontext.registerReceiver(blueReceiver, foundFilter);
    }


    @OnClick(R.id.text_cancle)
    public void cancle() {
        cancelDiscovery();
        mcontext.unregisterReceiver(blueReceiver);
        dismiss();
    }

    @OnClick(R.id.text_sure)
    public void sure() {
//        BluetoothUtil.writeOutputStream(mBlueSocket, "");
        dismiss();
    }

    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            beginDiscovery();
            mHandler.postDelayed(this, 2000);
        }
    };

    private void beginDiscovery() {
        if (mBluetooth.isDiscovering() != true) {
            lstDevices.clear();
            mBluetooth.startDiscovery();
        }
    }

    /**
     * 取消搜索蓝牙设备
     */
    private void cancelDiscovery() {
        if (mBluetooth.isDiscovering() == true) {
            mBluetooth.cancelDiscovery();
        }
    }

    //刷新已连接的状态
    private void refreshAddress(String address) {
        for (int i = 0; i < lstDevices.size(); i++) {
            BlueDeviceBean item = lstDevices.get(i);
            if (item.getAddress().equals(address)) {
                item.setBondState(3);
                lstDevices.set(i, item);
            }
        }
        listAdapter.blueDevicelist = lstDevices;
        listAdapter.notifyDataSetChanged();
    }

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("", "onReceive action=" + action);
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BlueDeviceBean blueDeviceBean = new BlueDeviceBean();
                blueDeviceBean.setAddress(device.getAddress());
                blueDeviceBean.setName(device.getName());
                blueDeviceBean.setBondState(device.getBondState() - 10);
                //过滤重复的蓝牙设备
                if(blueDeviceBean.getAddress() != null){
                    for(BlueDeviceBean bb : lstDevices){
                        if(blueDeviceBean.getAddress().equals(bb.getAddress())){
                            return;
                        }
                    }
                    lstDevices.add(blueDeviceBean);
                }
                listAdapter.blueDevicelist = lstDevices;
                listAdapter.notifyDataSetChanged();
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                mHandler.removeCallbacks(mRefresh);
                ToastUtils.show(mcontext, "蓝牙设备搜索完成");
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                    ToastUtils.show(mcontext, "正在配对" + device.getName());
                } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    ToastUtils.show(mcontext, "完成配对" + device.getName());
                    mHandler.postDelayed(mRefresh, 50);
                } else if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    ToastUtils.show(mcontext, "取消配对" + device.getName());
                }
            }else if(action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) { //再次得到的action，会等于PAIRING_REQUEST
                Log.e("action2=", action);
//                if (device.getName().contains("HC-05")) {
                    Log.e("here", "OKOKOK");
                    try {
                        //1.确认配对
                        ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                        //2.终止有序广播
                        Log.e("order...", "isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                        abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                        //3.调用setPin方法进行配对...
                        boolean ret = ClsUtils.setPin(device.getClass(), device, "0000");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//                }
            }
        }
    }

    /**
     * 确定位置
     */
    private void setView() {
        view.setAlpha(1.0f);
        this.setContentView(view);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) mcontext.getResources().getDisplayMetrics().widthPixels; // 宽度
        dialogWindow.setAttributes(lp);
    }


    /**
     * gatt连接结果的返回
     */
    private List<BluetoothGattService> mServiceList;
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /**
         * Callback indicating when GATT client has connected/disconnected to/from a remote GATT server
         *
         * @param gatt 返回连接建立的gatt对象
         * @param status 返回的是此次gatt操作的结果，成功了返回0
         * @param newState 每次client连接或断开连接状态变化，STATE_CONNECTED 0，STATE_CONNECTING 1,STATE_DISCONNECTED 2,STATE_DISCONNECTING 3
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.e("bluetooth", "onConnectionStateChange status:" + status + "  newState:" + newState);
            if (status == 0) {
                gatt.discoverServices();
            }
        }

        /**
         * Callback invoked when the list of remote services, characteristics and descriptors for the remote device have been updated, ie new services have been discovered.
         *
         * @param gatt 返回的是本次连接的gatt对象
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("bluetooth", "onServicesDiscovered status" + status);
            mServiceList = gatt.getServices();
            if (mServiceList != null) {
                System.out.println(mServiceList);
                System.out.println("Services num:" + mServiceList.size());
            }

            for (BluetoothGattService service : mServiceList){
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                System.out.println("扫描到Service：" + service.getUuid());

                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    System.out.println("characteristic: " + characteristic.getUuid() );
                }
            }
        }

        /**
         * Callback triggered as a result of a remote characteristic notification.
         *
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d("bluetooth", "onCharacteristicChanged");
        }

        /**
         * Callback indicating the result of a characteristic write operation.
         *
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d("bluetooth", "onCharacteristicWrite");
        }

        /**
         *Callback reporting the result of a characteristic read operation.
         *
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d("bluetooth", "onCharacteristicRead");
        }

        /**
         * Callback indicating the result of a descriptor write operation.
         *
         * @param gatt
         * @param descriptor
         * @param status
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d("bluetooth", "onDescriptorWrite");
        }

    };
}
