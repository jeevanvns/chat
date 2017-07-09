package com.editsoft.ansh.mychat.utility;

import android.content.Context;
import android.content.SharedPreferences;


import com.editsoft.ansh.mychat.MyChat;

import java.util.Set;


public class PreferenceUtils {

    private static final String AppPreference = "MyChat";

    private final static SharedPreferences preferences = MyChat.context.getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

    public static String getString(String key) {
        return preferences.getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static int getInt(String key, Integer defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    /**
     * Add String Value In SharedPreferences
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * Add Boolean Value In SharedPreferences
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Add Long Value In SharedPreferences
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }


    public static long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    /**
     * Add Int Value In SharedPreferences
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);

    }

    /**
     * Add Set <String>  Value In SharedPreferences
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * remove Particular  Value In SharedPreferences using Key
     *
     * @param key
     * @return
     */
    public static boolean removeKey(String key) {
        if (preferences.contains(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            return editor.commit();
        }
        return false;
    }

    /**
     * remove all Value in SharedPreferences
     */
    public static void removeAll() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
