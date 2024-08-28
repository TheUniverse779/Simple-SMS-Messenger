package com.messenger.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User {

    public String getAvatar(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("avatar","");
    }
    public void setAvatar(Context context,String url){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("avatar",url).apply();
    }

    public String getName(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("name","Account");
    }
    public void setName(Context context,String name){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("name",name).apply();
    }
    public String getPassCode(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("passCode","000");
    }
    public void setPassCode(Context context,String pc){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("passCode",pc).apply();
    }
    public Boolean isLock(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("lock",false);
    }
    public void setLock(Context context,boolean isLock){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("lock",isLock).apply();
    }

    public Boolean isLockScreen(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("lock_screen",false);
    }
    public void setLockScreen(Context context,boolean isLock){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("lock_screen",isLock).apply();
    }

}
