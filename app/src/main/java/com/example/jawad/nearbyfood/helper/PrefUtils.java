package com.example.jawad.nearbyfood.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;


/**
 * Helper class to deal with the shared preferences
 * It contains put and get methods for integers,booleans, and strings
 * also added more methods such as saveQBUser to ease up things a little
 * All rights reserved
 * MIT LICENSED
 */
public class PrefUtils {

    // , Context.MODE_PRIVATE
    public static int getInt(Context context, int key, int defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getInt(context.getString(key), defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getInt(key, defValue);
    }

    public static float getFloat(Context context, String key, float defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getFloat(key, defValue);
    }

    public static String getString(Context context, int key, String defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getString(context.getString(key), defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getString(key, defValue);
    }

    public static boolean getBoolean(Context context, int key, boolean defValue) {
        SharedPreferences
                pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(key), defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getBoolean(key, defValue);
    }

    public static boolean setString(Context context, String key, String value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean setBoolean(Context context, int key, boolean value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(context.getString(key), value);
        return editor.commit();
    }

    public static boolean setInt(Context context, int key, int value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(key), value);
        return editor.commit();
    }

    public static boolean setInt(Context context, String key, int value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean setFloat(Context context, String key, float value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static boolean setStringSet(Context context, String key, Set<String> value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences
                pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getStringSet(key, new HashSet<String>());
    }

    public static void clearAll(Context context) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().apply();
    }

    public static void clearSpecificPref(Context context, String key) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().remove(key).commit();
    }
}