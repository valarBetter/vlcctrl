package com.example.vlcctrl;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {
    private static final String PREFS_NAME = "VLC_PREF";

    public static void saveIP(Context context, String ip) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("ip", ip);
        editor.apply();
    }

    public static void savePassword(Context context, String pwd) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("pwd", pwd);
        editor.apply();
    }

    public static String getIP(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString("ip", null);
    }

    public static String getPassword(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString("pwd", null);
    }
}
