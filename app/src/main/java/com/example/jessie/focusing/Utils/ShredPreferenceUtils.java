package com.example.jessie.focusing.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 19:21
 */
public class ShredPreferenceUtils {
    private volatile static ShredPreferenceUtils instance;
    private Context context;
    private SharedPreferences sharedPreferences;

    private ShredPreferenceUtils() {
    }

    public static ShredPreferenceUtils getInstance() {
        if (null == instance) {
            synchronized (ShredPreferenceUtils.class) {
                if (null == instance) {
                    instance = new ShredPreferenceUtils();
                }
            }
        }
        return instance;
    }


    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
        }
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        }
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public long getLong(String key, int defInt) {
        return sharedPreferences.getLong(key, defInt);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public long getInt(String key, int defInt) {
        return sharedPreferences.getInt(key, defInt);
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }


    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
