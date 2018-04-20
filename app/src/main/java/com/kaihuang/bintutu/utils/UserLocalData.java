package com.kaihuang.bintutu.utils;

import android.content.Context;


public class UserLocalData {


    public static void putToken(Context context, String token) {

        PreferencesUtils.putString(context, Contants.TOKEN, token);
    }

    public static String getToken(Context context) {

        return PreferencesUtils.getString(context, Contants.TOKEN);

    }

    public static void clearToken(Context context) {

        PreferencesUtils.putString(context, Contants.TOKEN, "");
    }

    public static void putUserID(Context context, String userID) {
        PreferencesUtils.putString(context, Contants.USER_ID, userID);
    }

    public static String getUserID(Context context) {
        return PreferencesUtils.getString(context, Contants.USER_ID);
    }


    public static void clearUserID(Context context) {
        PreferencesUtils.putString(context, Contants.USER_ID, "");
    }


}
