package com.training.cinematic.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.training.cinematic.BuildConfig;

import java.io.Serializable;

public class SharedPrefsHelp {
    private static final String prefFileNmae = BuildConfig.APPLICATION_ID + ".pref";

    public static void setString (Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setObject (Context context, String key, Serializable value) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, new Gson().toJson(value));
        editor.apply();
    }

    public static  void setBoolean (Context context, String key, Boolean value) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static  String getString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        return settings.getString(key, defValue);
    }

    public  static Boolean getBoolean(Context context, String key, Boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        return settings.getBoolean(key, defValue);
    }

    public static  String getObject(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(prefFileNmae, 0);
        return settings.getString(key, defValue);
    }

    public static  void clearSharedPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(prefFileNmae, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
    }
}