package com.messenger.helper;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.messenger.App;
import com.messenger.callbacks.OGCallback;
import com.messenger.models.DeviceApp;
import com.messenger.models.LockApp;
import com.messenger.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LockDatabase {

    private static final String DB_NAME = "my.db";

    private static LockDatabase sqDatabase;

    private SQLiteDatabase db;

    private List<OGCallback> synCbList = new ArrayList<>();

    private String[] appAddFirst = new String[]{"com.facebook.katana","com.facebook.orca","com.zing.zalo","com.android.chrome"
    ,"com.whatsapp","com.tencent.mm","com.skype.raider","com.ss.android.ugc.trill","com.yy.hiyo",
    "com.facebook.lite","com.facebook.mlite",""};

    private boolean isLoaded = true;

    private String appCurrent = "android";

    public static LockDatabase get(){
        if(sqDatabase ==null)
            sqDatabase = new LockDatabase();
        return sqDatabase;
    }
    private LockDatabase(){
        if(db==null){
           syn(App.get());
        }
    }

    public void synDb(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                isLoaded = false;
                initApps();
                isLoaded = true;

                for (OGCallback synCb:synCbList){
                    synCb.callback(0);
                }

            }
        }.start();
    }

    public void reloadDb(OGCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                isLoaded = false;
                reloadApps(callback);
                isLoaded = true;

            }
        }.start();
    }




    public void getInstallApp(OGCallback onLoadInstall){
        if(isLoaded){
            getInstallAppData(onLoadInstall);
        } else {
            synCbList.add(ob -> getInstallAppData(onLoadInstall));
        }
    }

    public void getLockApp(OGCallback onLoadLockApp){
        if(isLoaded){
            getLockAppData(onLoadLockApp);
        } else {
            synCbList.add(ob -> getInstallAppData(onLoadLockApp));
        }
    }

    private void getLockAppData(OGCallback onLoadLockApp) {
        ArrayList<LockApp> lockApps = new ArrayList<>();
        Cursor cursor  = db.rawQuery("" +
                "SELECT * " +
                "FROM app_show,app_lock " +
                "WHERE app_lock.appid = app_show.appid",null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                String appId = cursor.getString(0);
                String name = cursor.getString(1);
                long timeOpen;
                try {
                    timeOpen = Long.parseLong(cursor.getString(3));
                }catch (Exception e){
                    timeOpen = 0;
                }

                boolean isLock = cursor.getInt(4) == 1;
                String pin = cursor.getString(5);

                LockApp lockApp = new LockApp();
                lockApp.setAppId(appId);
                lockApp.setName(name);
                lockApp.setIcon(Utils.getIconFromPkn(appId));
                lockApp.setLock(isLock);
                lockApp.setTimeOpen(timeOpen);
                lockApp.setPin(pin);
                if(lockApp.getIcon()!=null)
                lockApps.add(lockApp);
            }while (cursor.moveToNext());
            cursor.close();
        }
        onLoadLockApp.callback(lockApps);
    }

    private void getInstallAppData(OGCallback callback){
        ArrayList<DeviceApp> deviceApps = new ArrayList<>();
        Cursor cursor  = db.rawQuery("SELECT * FROM device_app",null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                String appId = cursor.getString(0);
                String name = cursor.getString(1);
                DeviceApp deviceApp = new DeviceApp();
                deviceApp.setAppId(appId);
                deviceApp.setName(name);
                deviceApp.setIcon(Utils.getIconFromPkn(appId));
                deviceApp.setCheck(checkAppIsShowLock(appId));
                if(deviceApp.getIcon()!=null)
                deviceApps.add(deviceApp);
            }while (cursor.moveToNext());
            cursor.close();
        }
        callback.callback(deviceApps);
    }


    private boolean checkAppIsShowLock(String appId){
        Cursor cursor = db.rawQuery("SELECT * FROM app_show WHERE appid = '"+appId+"'",null);
        if(cursor!=null&&cursor.moveToFirst()){
            cursor.close();
            return true;
        }else return false;
    }

    public boolean isDeviceAppEmpty(){
        boolean empty = true;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM device_app", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
            cur.close();
        }
        return empty;
    }

    private void initApps(){
        List<String> checkApp = Arrays.asList(appAddFirst);
        db.execSQL("DELETE from device_app");
        PackageManager pm = App.get().getPackageManager();
        Intent ii = new Intent(Intent.ACTION_MAIN);
        ii.addCategory(Intent.CATEGORY_LAUNCHER);
            ArrayList<ResolveInfo> mList = (ArrayList<ResolveInfo>) pm
                .queryIntentActivities(ii, 0);
        Collections.sort(mList, new ResolveInfo.DisplayNameComparator(pm));
        for (int i=0;i<mList.size();i++){
            String pkn = mList.get(i).activityInfo.applicationInfo.packageName;

            if(App.get().getPackageName().equals(pkn))continue;

            String name = mList.get(i).loadLabel(pm).toString();
            ContentValues valueApp = new ContentValues();
            valueApp.put("appid",pkn);
            valueApp.put("name",name);
            db.insert("device_app",null,valueApp);

            if(checkApp.contains(pkn)){
                ContentValues valueShow = new ContentValues();
                valueShow.put("appid",pkn);
                valueShow.put("name",name);
                db.insert("app_show",null,valueShow);

                ContentValues valueLock = new ContentValues();
                valueLock.put("appid",pkn);
                valueLock.put("timeopen","0");
                valueLock.put("islock",0);
                valueLock.put("pin","0000");

                db.insert("app_lock",null,valueLock);
            }
        }
    }

    private void reloadApps(OGCallback callback){
        db.execSQL("DELETE from device_app");
        PackageManager pm = App.get().getPackageManager();
        Intent ii = new Intent(Intent.ACTION_MAIN);
        ii.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> mList = (ArrayList<ResolveInfo>) pm
                .queryIntentActivities(ii, 0);
        Collections.sort(mList, new ResolveInfo.DisplayNameComparator(pm));
        for (int i=0;i<mList.size();i++){
            String pkn = mList.get(i).activityInfo.applicationInfo.packageName;
            if(App.get().getPackageName().equals(pkn))continue;
            String name = mList.get(i).loadLabel(pm).toString();
            ContentValues valueApp = new ContentValues();
            valueApp.put("appid",pkn);
            valueApp.put("name",name);
            db.insert("device_app",null,valueApp);
            callback.callback(i+"/"+mList.size());
        }
        callback.callback(0);
    }

    public void upTimeOpen(LockApp lockApp) {
        long time = lockApp.getTimeOpen()+1;
        db.execSQL("UPDATE app_lock SET timeopen = '"+time+"' WHERE appid = '"+lockApp.getAppId()+"'");
    }

    public void updateAppLock(String id,boolean b, String passCheck) {
        int lock = b?1:0;
        db.execSQL("UPDATE app_lock SET islock = '"+lock+"', pin = '"+passCheck+"' WHERE appid = '"+id+"'");
    }

    public boolean isLockAppEnableEmpty() {
        Cursor cursor  = db.rawQuery("" +
                "SELECT * " +
                "FROM app_show,app_lock " +
                "WHERE app_lock.appid = app_show.appid AND app_lock.islock = '1' ",null);
        if(cursor!=null&&cursor.moveToFirst()){
            cursor.close();
            return false;
        }else {
            return true;
        }
    }

    public LockApp getLockApp(String appId){
        Cursor cursor  = db.rawQuery("" +
                "SELECT * " +
                "FROM app_show,app_lock " +
                "WHERE app_lock.appid = app_show.appid AND app_lock.appid = '"+appId+"' ",null);
        if(cursor!=null&&cursor.moveToFirst()){
            String name = cursor.getString(1);
            long timeOpen;
            try {
                timeOpen = Long.parseLong(cursor.getString(3));
            }catch (Exception e){
                timeOpen = 0;
            }
            boolean isLock = cursor.getInt(4) == 1;
            String pin = cursor.getString(5);
            LockApp lockApp = new LockApp();
            lockApp.setAppId(appId);
            lockApp.setName(name);
            lockApp.setIcon(Utils.getIconFromPkn(appId));
            lockApp.setLock(isLock);
            lockApp.setTimeOpen(timeOpen);
            lockApp.setPin(pin);
            cursor.close();
            return lockApp;
        }else {
            return null;
        }
    }

    public void saveAppShow(ArrayList<DeviceApp> deviceApps,OGCallback callback) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                db.execSQL("DELETE from app_show");
                for (DeviceApp ap:deviceApps){
                    if(ap.isCheck()){
                        ContentValues valueShow = new ContentValues();
                        valueShow.put("appid",ap.getAppId());
                        valueShow.put("name",ap.getName());
                        db.insert("app_show",null,valueShow);

                        ContentValues valueLock = new ContentValues();
                        valueLock.put("appid",ap.getAppId());
                        valueLock.put("timeopen","0");
                        valueLock.put("islock",0);
                        valueLock.put("pin","0000");
                        db.insert("app_lock",null,valueLock);
                    }
                }
                callback.callback(0);
            }
        }.start();

    }
    private void syn(Context context) {
        File file = context.getDatabasePath(DB_NAME);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(DB_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024 * 8];
            int numOfBytesToRead;
            try {
                while((numOfBytesToRead = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, numOfBytesToRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        db = SQLiteDatabase.openOrCreateDatabase(file, null);
    }


    public boolean isAppEnableLock() {
        Cursor cursor = db.rawQuery("SELECT islock FROM app_lock WHERE appid = '"+appCurrent+"'",null);
        if(cursor!=null&&cursor.moveToFirst()){
            int i = cursor.getInt(0);
            cursor.close();
            return i==1;
        }else {
            return false;
        }
    }

    public String getAppCurrent() {
        return appCurrent;
    }

    public void setAppCurrent(String appCurrent) {
        this.appCurrent = appCurrent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public String getAppCurrent(String appCurrent){
        UsageStatsManager usageStatsManager = (UsageStatsManager) App.get().getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents events = usageStatsManager.queryEvents(System.currentTimeMillis()-60000,System.currentTimeMillis());
        UsageEvents.Event event = new UsageEvents.Event();
        while (events.hasNextEvent()){
            events.getNextEvent(event);
        }
        if(event.getEventType()==UsageEvents.Event.MOVE_TO_FOREGROUND){
            return event.getPackageName()+"";
        }else {
            return appCurrent ;
        }
    }


}
