package com.editsoft.ansh.mychat.utility;

/**
 *  Created by SurvivoR on 7/10/2017.
 */

public class PreferenceHelper {
    private static String name = "name";
    private static String mobileNo = "mobileNo";
    private static String isLogin = "isLogin";

    public static String getName() {
        return PreferenceUtils.getString(name);
    }

    public static void setName(String value) {
        PreferenceUtils.putString(name, value);
    }

    public static String getMobileNo() {
        return PreferenceUtils.getString(mobileNo);
    }

    public static void setMobileNo(String value) {
        PreferenceUtils.putString(mobileNo, value);
    }

    public static boolean getIsLogin() {
        return PreferenceUtils.getBoolean(isLogin);
    }

    public static void setIsLogin(boolean value) {
        PreferenceUtils.putBoolean(isLogin, value);
    }
}
