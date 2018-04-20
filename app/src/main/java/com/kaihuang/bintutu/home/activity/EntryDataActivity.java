package com.kaihuang.bintutu.home.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaihuang.bintutu.R;
import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseApplication;
import com.kaihuang.bintutu.common.http.SpotsCallBack;
import com.kaihuang.bintutu.common.model.BaseRespMsg;
import com.kaihuang.bintutu.home.adapter.CardAdapter;
import com.kaihuang.bintutu.home.adapter.FootDescDetailAdapter;
import com.kaihuang.bintutu.home.adapter.UploadFootAdapter;
import com.kaihuang.bintutu.home.bean.ShowTagPictureBean;
import com.kaihuang.bintutu.home.bean.TagPictureDesc;
import com.kaihuang.bintutu.utils.BintutuUtils;
import com.kaihuang.bintutu.utils.BlurBitmapUtils;
import com.kaihuang.bintutu.utils.Contants;
import com.kaihuang.bintutu.utils.Utils;
import com.kaihuang.bintutu.utils.ViewSwitchUtils;
import com.kaihuang.bintutu.utilviews.CardScaleHelper;
import com.kaihuang.bintutu.utilviews.DialogUtil;
import com.kaihuang.bintutu.utilviews.PictureTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jameson.io.library.util.ToastUtils;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 手动输入足型数据
 */

public class EntryDataActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CardAdapter.OnItemClickLitener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.recycler_foot_desc_add)
    RecyclerView recycler_foot_desc_add;
    @Bind(R.id.recycler_footdetail)
    RecyclerView recycler_footdetail;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Bind(R.id.rel_edit_phone)
    RelativeLayout rel_edit_phone;
    @Bind(R.id.rel_edit_scanid)
    RelativeLayout rel_edit_scanid;
    @Bind(R.id.edit_username)
    EditText edit_username;
    @Bind(R.id.edit_weight)
    EditText edit_weight;
    @Bind(R.id.edit_height)
    EditText edit_height;
    @Bind(R.id.edit_shoesize)
    EditText edit_shoesize;
    @Bind(R.id.rel_default)
    RelativeLayout rel_default;
    @Bind(R.id.blurView)
    ImageView blurView;

    @Bind(R.id.radiogroup_sex)
    RadioGroup radiogroup_sex;//性别选择
    @Bind(R.id.radiogroup_foot_change_add)
    RadioGroup radiogroup_foot_change_add;//左右脚切换
    @Bind(R.id.radiogroup_left_neiwaifan)
    RadioGroup radiogroup_left_neiwaifan;//左脚内外翻评估
    @Bind(R.id.radiogroup_right_neiwaifan)
    RadioGroup radiogroup_right_neiwaifan;//右脚内外翻评估
    @Bind(R.id.radiogroup_left_zugong)
    RadioGroup radiogroup_left_zugong;//左脚足弓评估
    @Bind(R.id.radiogroup_right_zugong)
    RadioGroup radiogroup_right_zugong;//右脚足弓评估
    @Bind(R.id.text_recommend_size)
    TextView text_recommend_size;//系统推荐尺码

    private UploadFootAdapter uploadFootAdapter = null;
    private String[] leftfoots = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private String[] rightfoots = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    private String[] footDataParaIDs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37"};
    private boolean isLeftFoot = true;

    private String sex = "1";
    private String userName;
    private String weight;
    private String height;
    private String shoesize;

    private List<String> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private static final int REQUEST_CODE = 100;
    private List<ShowTagPictureBean> leftFootPictureList = new ArrayList<>();//展示左脚编辑后的图数据
    private List<ShowTagPictureBean> rightFootPictureList = new ArrayList<>();//展示右脚脚编辑后的图数据
    private List<Integer> originaLeftlList = new ArrayList<>();//原始左脚示例图数据
    private List<Integer> originaRightlList = new ArrayList<>();//原始右脚示例图数据
    private List<Integer> myList = new ArrayList<>();
    private String leftRightFoot = "1";//1 默认为左脚 2.为右脚
    private CardAdapter cardAdapter;
    private FootDescDetailAdapter mFootDescDetailAdapter;

    private String leftneiwaifandu = "";//左脚内外翻度
    private String rightneiwaifandu = "";//右脚内外翻度
    private String leftzugongdu = "";//左脚足弓度
    private String rightzugongdu = "";//右脚足弓度

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback netcallback = null;

    private DialogUtil dialogUtil = new DialogUtil();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_entry_data;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        rel_edit_phone.setVisibility(View.GONE);
        rel_edit_scanid.setVisibility(View.GONE);
        //初始化手动输入量脚的内容
        uploadFootAdapter = new UploadFootAdapter(BaseApplication.getInstance());
        uploadFootAdapter.isLeftFoot = isLeftFoot;
        uploadFootAdapter.leftfoots = leftfoots;
        uploadFootAdapter.rightfoots = rightfoots;
        uploadFootAdapter.setGetTextString(new UploadFootAdapter.GetTextString() {
            @Override
            public void keepTextString(int position, String info,boolean isLeftFoot) {
                if (isLeftFoot) {
                    leftfoots[position] = info;
                } else {
                    rightfoots[position] = info;
                    if(position == 0){
                        if(!TextUtils.isEmpty(BintutuUtils.recommdSize(Integer.parseInt(info)))){
                            text_recommend_size.setText(BintutuUtils.recommdSize(Integer.parseInt(info)) + "码");
                        }else{
                            text_recommend_size.setText("暂无推荐尺码");
                        }
                    }
                }
            }
        });
        //解决recyclerview 卡顿的问题
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(BaseApplication.getInstance()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler_footdetail.setLayoutManager(linearLayoutManager1);
        recycler_footdetail.setAdapter(uploadFootAdapter);

        //细节描述
        mFootDescDetailAdapter = new FootDescDetailAdapter(BaseApplication.getInstance(),"");
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(BaseApplication.getInstance()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler_foot_desc_add.setLayoutManager(linearLayoutManager2);
        recycler_foot_desc_add.setAdapter(mFootDescDetailAdapter);

        btn_submit.setText("上传");
    }

    @Override
    protected void initEvents() {
        radiogroup_sex.setOnCheckedChangeListener(this);//性别监听
        radiogroup_foot_change_add.setOnCheckedChangeListener(this);//左右脚监听
        radiogroup_left_neiwaifan.setOnCheckedChangeListener(this);//左脚内外翻评估
        radiogroup_right_neiwaifan.setOnCheckedChangeListener(this);//右脚内外翻评估
        radiogroup_left_zugong.setOnCheckedChangeListener(this);//左脚足弓评估
        radiogroup_right_zugong.setOnCheckedChangeListener(this);//右脚足弓评估
    }

    /**
     * 男女性别选择, 左右脚选择
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group == radiogroup_foot_change_add){//左右脚选择
            List<String> delist = new ArrayList<>();
            int pos = 0;
            switch (checkedId) {
                case R.id.radio_left_foot_add:
                    leftRightFoot = "1";
                    cardAdapter.showTagPictureBeanList = leftFootPictureList;
                    delist = leftFootPictureList.get(mLastPos).getDescList();
                    pos = mLastPos + 1;
                    break;
                case R.id.radio_right_foot_add:
                    leftRightFoot = "2";
                    cardAdapter.showTagPictureBeanList = rightFootPictureList;
                    delist= rightFootPictureList.get(mLastPos).getDescList();
                    pos = mLastPos + 5;
                    break;
            }
            mFootDescDetailAdapter.list = BintutuUtils.singleList(delist,pos);
            mFootDescDetailAdapter.notifyDataSetChanged();
            cardAdapter.notifyDataSetChanged();
        }else if(group == radiogroup_sex){//性别选择
            switch (checkedId) {
                case R.id.radiobtn_man:
                    sex = "1";
                    break;
                case R.id.radiobtn_woman:
                    sex = "2";
                    break;
            }
        }else if(group == radiogroup_left_neiwaifan){//左脚内外翻评估
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            leftneiwaifandu = radioButton.getText().toString();
        }else if(group == radiogroup_right_neiwaifan){//右脚内外翻评
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            rightneiwaifandu = radioButton.getText().toString();
        }else if(group == radiogroup_left_zugong){//左脚足弓评估
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            leftzugongdu = radioButton.getText().toString();
        }else if(group == radiogroup_right_zugong){//右脚足弓评估
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            rightzugongdu = radioButton.getText().toString();
        }
    }
    @Override
    protected void initData() {
        assembleInitData();
        initImgData();
        initNetWork();
    }


    /**
     * 初始化足型数据示例图
     */
    private void initImgData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        cardAdapter = new CardAdapter(this, leftFootPictureList);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickLitener(this);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(recyclerView);
        initBlurBackground();
    }

    private void initBlurBackground() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });
        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        if("1".equals(leftRightFoot)){
            myList = originaLeftlList;
        }else if("2".equals(leftRightFoot)){
            myList = originaRightlList;
        }
        if (mCardScaleHelper.getCurrentItemPos() >= 0 && mCardScaleHelper.getCurrentItemPos() < mList.size()) {
            final int resId = myList.get(mCardScaleHelper.getCurrentItemPos());
            blurView.removeCallbacks(mBlurRunnable);
            mBlurRunnable = new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                    ViewSwitchUtils.startSwitchBackgroundAnim(blurView, BlurBitmapUtils.getBlurBitmap(blurView.getContext(), bitmap, 15));
                }
            };
            blurView.postDelayed(mBlurRunnable, 500);
        }
        //滑动的时候更新下面的详情描述信息
        List<String> delist = new ArrayList<>();
        int pos = 0;
        if("1".equals(leftRightFoot)){
            delist = leftFootPictureList.get(mLastPos).getDescList();
            pos = mLastPos + 1;
        }else if("2".equals(leftRightFoot)){
            delist = rightFootPictureList.get(mLastPos).getDescList();
            pos = mLastPos + 5;
        }
        mFootDescDetailAdapter.list = BintutuUtils.singleList(delist,pos);;
        mFootDescDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 点击图片跳转进行编辑
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(BaseApplication.getInstance(), PictureTagActivity.class);
        if("1".equals(leftRightFoot)){
            myList = originaLeftlList;
        }else if("2".equals(leftRightFoot)){
            myList = originaRightlList;
        }
        intent.putExtra("imageId",myList.get(position));
        intent.putExtra("leftRightFoot",leftRightFoot);
        intent.putExtra("position",position);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if(data != null){
                    String fileName = data.getStringExtra("fileName");
                    String leftRightFoot = data.getStringExtra("leftRightFoot");
                    int position = data.getIntExtra("position",0);
                    ArrayList<String> descList = data.getStringArrayListExtra("descList");
                    ShowTagPictureBean showTagPictureBean = new ShowTagPictureBean();
                    showTagPictureBean.setFileName(fileName);
                    showTagPictureBean.setIsFrom("1");

                    showTagPictureBean.setDescList(descList);
                    List<TagPictureDesc> list = new ArrayList<>();
                    if("1".equals(leftRightFoot)){
                        showTagPictureBean.setPosition(position + 1);
                        //
                        if(TextUtils.isEmpty(fileName)) {
                            showTagPictureBean.setIsFrom("");
                            showTagPictureBean.setFileName(originaLeftlList.get(position) + "");
                        }
                        //移除相应位置的数据，将编辑后的数据插
                        leftFootPictureList.remove(position);
                        leftFootPictureList.add(position,showTagPictureBean);
                        cardAdapter.showTagPictureBeanList = leftFootPictureList;
                    }else if("2".equals(leftRightFoot)){
                        showTagPictureBean.setPosition(position + 5);
                        if(TextUtils.isEmpty(fileName)) {
                            showTagPictureBean.setIsFrom("");
                            showTagPictureBean.setFileName(originaLeftlList.get(position) + "");
                        }
                        rightFootPictureList.remove(position);
                        rightFootPictureList.add(position,showTagPictureBean);
                        cardAdapter.showTagPictureBeanList = rightFootPictureList;
                    }
                    mFootDescDetailAdapter.list = BintutuUtils.singleList(descList,showTagPictureBean.getPosition());
                    mFootDescDetailAdapter.notifyDataSetChanged();
                    cardAdapter.notifyDataSetChanged();
                    if(mFootDescDetailAdapter.list != null && mFootDescDetailAdapter.list.size() > 0){
                        rel_default.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    /**
     * 上传足型数据
     */
    @OnClick(R.id.btn_submit)
    public void upInfo() {
        userName = edit_username.getText().toString().trim();
        weight = edit_weight.getText().toString().trim();
        height = edit_height.getText().toString().trim();
        shoesize = edit_shoesize.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show(EntryDataActivity.this, "请输入姓名");
        } else if (TextUtils.isEmpty(sex)) {
            ToastUtils.show(EntryDataActivity.this, "请选择性别");
        } else if (TextUtils.isEmpty(weight)) {
            ToastUtils.show(EntryDataActivity.this, "请输入体重");
        } else if (TextUtils.isEmpty(height)) {
            ToastUtils.show(EntryDataActivity.this, "请输入身高");
        } else if (TextUtils.isEmpty(shoesize)) {
            ToastUtils.show(EntryDataActivity.this, "请输入鞋码");
        } else {
            dialogUtil.infoDialog(EntryDataActivity.this, "确定上传数据", null, true, true);
            dialogUtil.setOnClick(new DialogUtil.OnClick() {
                @Override
                public void leftClick() {
                    dialogUtil.dialog.dismiss();
                }

                @Override
                public void rightClick() {
                    dialogUtil.dialog.dismiss();
                    dialogUtil.showLoadingDialog(EntryDataActivity.this, "正在上传数据...");
                    submitInfo();
                }
            });
        }
    }

    /**
     * 拼装json
     */
    private void submitInfo() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("name", userName);
            params.put("sex", sex);
            params.put("stature", height);
            params.put("weight", weight);
            params.put("shoeSize", shoesize);
            leftfoots[35] = leftzugongdu;
            rightfoots[35] = rightzugongdu;
            leftfoots[36] = leftneiwaifandu;
            rightfoots[36] = rightneiwaifandu;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < footDataParaIDs.length; i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("leftData", leftfoots[i]);
                    jsonObject.put("rightData", rightfoots[i]);
                    jsonObject.put("footDataParaID", footDataParaIDs[i]);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            params.put("footmsg", jsonArray.toString());
            params.put("phone", Contants.bindPhone);
            params.put("footArch", "0");
            params.put("footVarusValgusID", "0");
            params.put("salesmanID", BaseApplication.getInstance().getUserID());
            params.put("equipmentid", "0");
            params.put("bindingUserID", Contants.bindingUserID);
            if(TextUtils.isEmpty(Contants.relateUserFootId)){
                params.put("userfoottypedatarelateid",0);
            }else{
                params.put("userfoottypedatarelateid",Contants.relateUserFootId);
            }
            JSONArray jsonArray1 = BintutuUtils.objToJson(leftFootPictureList, rightFootPictureList);
            params.put("imgPath", jsonArray1);
            params.put("token", BaseApplication.getInstance().getToken());
            upLoadInfo(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void upLoadInfo(Map<String, Object> params) {
        mHttpHelper.post(Contants.API.saveUserFootData, params, new SpotsCallBack<BaseRespMsg>(this,"msg") {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if ("success".equals(baseRespMsg.getStatus())) {
                    if(netcallback != null){
                        connectivityManager.unregisterNetworkCallback(netcallback);//取消指定绑定网络
                    }
                    finish();
                }
                ToastUtils.show(EntryDataActivity.this, baseRespMsg.getMessage());
                dialogUtil.dismissLoading();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(EntryDataActivity.this, "请求失败，请稍后重试");
                dialogUtil.dismissLoading();
            }

            @Override
            public void onServerError(Response response, int code, String errmsg) {
                ToastUtils.show(EntryDataActivity.this, "请求失败，请稍后重试");
                dialogUtil.dismissLoading();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                ToastUtils.show(EntryDataActivity.this, "请求失败，请稍后重试");
                dialogUtil.dismissLoading();
            }
        });
    }

    private boolean isHasEmpty(String[] footinfo) {
        for (int i = 0; i < footinfo.length; i++) {
            if (TextUtils.isEmpty(footinfo[i])) {
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.imgbtn_back)
    public void backtopre(View view) {
        if(netcallback != null){
            connectivityManager.unregisterNetworkCallback(netcallback);//取消指定绑定网络
        }
        finish();
    }

    /**
     * 初始化网络为指定网络类型
     */
    private void initNetWork(){
        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        netcallback = new ConnectivityManager.NetworkCallback() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.e("test", "已根据功能和传输类型找到合适的网络");
                // 可以通过下面代码将app接下来的请求都绑定到这个网络下请求
                if (Build.VERSION.SDK_INT >= 23) {
                    connectivityManager.bindProcessToNetwork(network);
                } else {// 23后这个方法舍弃了
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            }
        };
        Utils.selectNetwork(connectivityManager,netcallback,EntryDataActivity.this);
    }

    /**
     * 拼装初始化数据
     */
    public void assembleInitData(){
        //左脚数据
        ShowTagPictureBean showTagPictureBean = new ShowTagPictureBean();
        showTagPictureBean.setFileName(R.drawable.leftfoot_top+ "");
        showTagPictureBean.setIsFrom("");
        showTagPictureBean.setLeftOrRight("1");
        leftFootPictureList.add(showTagPictureBean);

        ShowTagPictureBean showTagPictureBean1 = new ShowTagPictureBean();
        showTagPictureBean1.setFileName(R.drawable.leftfoot_button + "");
        showTagPictureBean1.setIsFrom("");
        showTagPictureBean1.setLeftOrRight("1");
        leftFootPictureList.add(showTagPictureBean1);

        ShowTagPictureBean showTagPictureBean2 = new ShowTagPictureBean();
        showTagPictureBean2.setFileName(R.drawable.leftfoot_inside + "");
        showTagPictureBean2.setIsFrom("");
        showTagPictureBean2.setLeftOrRight("1");
        leftFootPictureList.add(showTagPictureBean2);

        ShowTagPictureBean showTagPictureBean3 = new ShowTagPictureBean();
        showTagPictureBean3.setFileName(R.drawable.leftfoot_outside + "");
        showTagPictureBean3.setIsFrom("");
        showTagPictureBean3.setLeftOrRight("1");
        leftFootPictureList.add(showTagPictureBean3);

        //右脚数据
        ShowTagPictureBean showTagPictureBean4 = new ShowTagPictureBean();
        showTagPictureBean4.setFileName(R.drawable.rightfoot_top + "");
        showTagPictureBean4.setIsFrom("");
        showTagPictureBean4.setLeftOrRight("2");
        rightFootPictureList.add(showTagPictureBean4);

        ShowTagPictureBean showTagPictureBean5 = new ShowTagPictureBean();
        showTagPictureBean5.setFileName(R.drawable.rightfoot_button + "");
        showTagPictureBean5.setIsFrom("");
        showTagPictureBean5.setLeftOrRight("2");
        rightFootPictureList.add(showTagPictureBean5);

        ShowTagPictureBean showTagPictureBean6 = new ShowTagPictureBean();
        showTagPictureBean6.setFileName(R.drawable.rightfoot_inside + "");
        showTagPictureBean6.setIsFrom("");
        showTagPictureBean6.setLeftOrRight("2");
        rightFootPictureList.add(showTagPictureBean6);

        ShowTagPictureBean showTagPictureBean7 = new ShowTagPictureBean();
        showTagPictureBean7.setFileName(R.drawable.rightfoot_outside + "");
        showTagPictureBean7.setIsFrom("");
        showTagPictureBean7.setLeftOrRight("2");
        rightFootPictureList.add(showTagPictureBean7);

        originaLeftlList.add(R.drawable.leftfoot_top);
        originaLeftlList.add(R.drawable.leftfoot_button);
        originaLeftlList.add(R.drawable.leftfoot_inside);
        originaLeftlList.add(R.drawable.leftfoot_outside);

        originaRightlList.add(R.drawable.rightfoot_top);
        originaRightlList.add(R.drawable.rightfoot_button);
        originaRightlList.add(R.drawable.rightfoot_inside);
        originaRightlList.add(R.drawable.rightfoot_outside);
    }
}
