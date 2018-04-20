package com.kaihuang.bintutu.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;


public class Utils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    //弹出键盘
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (dip * density + 0.5f);
    }

    public static void bindStrText(TextView textView, String str) {
        if (textView == null || str == null) return;
        textView.setText(str);
    }

    /**
     * 将字符串转成数组
     * @param strin
     * @param c
     * @return
     */
    public static String[] split(String strin, String c) {
        if (strin == null) {
            return null;
        }
        ArrayList arraylist = new ArrayList();
        int begin = 0;
        int end = 0;
        while ((begin = strin.indexOf(c, end)) != -1) {
            String s2 = strin.substring(end, begin);
            if (s2.trim().length() > 0) { //
                arraylist.add(strin.substring(end, begin));
            }
            end = begin + c.length();
        }
        if (end != strin.length()) {
            arraylist.add(strin.substring(end));
        }
        int k = arraylist.size();
        String as[] = new String[k];
        return (String[]) arraylist.subList(0, k).toArray(as);
    }

    /**
     * 将list转成string
     * @param list
     * @return
     */
    public static String listToString(List<String> list){
        StringBuilder sb =new StringBuilder();
        boolean flag=false;
        if(list != null && list.size() > 0){
            for(int i=0;i < list.size();i++){
                if (flag) {
                    sb.append(",");
                }else {
                    flag=true;
                }
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    public static boolean isPkgInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 输入框下标转化到字体的最后面
     * @param editText
     * @param str
     */
    public static void cursorToEnd(EditText editText, String str){
        if(!TextUtils.isEmpty(str)){
            editText.setSelection(str.length());
            editText.requestFocus();  //这是关键
        }
    }

    /**
     * 关闭输入法的方法
     * @param context
     */
    public static void downSoftInput(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设定指定网络传输数据  TRANSPORT_CELLULAR 为手机数据网络
     * @param callback
     * @param context
     */
    public static void selectNetwork(ConnectivityManager connMgr,ConnectivityManager.NetworkCallback callback,Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(NET_CAPABILITY_INTERNET);
            // 设置指定的网络传输类型(蜂窝传输) 等于手机网络
            builder.addTransportType(TRANSPORT_CELLULAR);
            // 设置感兴趣的网络功能
//            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            // 设置感兴趣的网络：计费网络
//            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);

            NetworkRequest request = builder.build();
//            connMgr.registerNetworkCallback(request, callback);
            connMgr.requestNetwork(request, callback);
        }
    }

    /*
    * 外网是否可以访问
    * */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    * WIFI是否连接
    * */
    public static boolean isWifiNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
