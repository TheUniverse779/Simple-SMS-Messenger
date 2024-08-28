package com.messenger.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;
import com.messenger.App;
import com.messenger.models.MessApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class SqDatabase {

    private static final String TAG = "SqDatabase";

    private static final String DB_NAME = "ms.db";

    private static SqDatabase sqDatabase;

    private static User user;

    private SQLiteDatabase db;

    private static int TIKTOK_ID = 56;

    public static SqDatabase getDb(){
        if(sqDatabase ==null)
            sqDatabase = new SqDatabase();
        return sqDatabase;
    }

    public static User getUser(){
        if(user ==null)
            user = new User();
        return user;
    }

    public MessApp getTikTok(){
        SharedPreferences preferences = App.get().getSharedPreferences("tiktok",Context.MODE_PRIVATE);
        String tik = preferences.getString("tiktok","");
        MessApp messApp = null;
        try {
            messApp = new Gson().fromJson(tik,MessApp.class);
        }catch (Exception e){}
        if(messApp==null){
            messApp = new MessApp(TIKTOK_ID,"Tik Tok","https://tiktok.com/","tiktok",0,0,true,"TikTok",0);
        }

        return messApp;
    }

    public void setTikTok(MessApp messApp){
        SharedPreferences preferences = App.get().getSharedPreferences("tiktok",Context.MODE_PRIVATE);
        preferences.edit().putString("tiktok",new Gson().toJson(messApp)).apply();
    }

    public ArrayList<MessApp> getMessApps(Context context,Boolean isCheck){
        if(db==null)syn(context);
        ArrayList<MessApp> messApps = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM app",null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String icon = cursor.getString(2);
                String url = cursor.getString(3);
                long countOpen = Long.parseLong(cursor.getString(4));
                long timeOpen = Long.parseLong(cursor.getString(5));
                boolean check = cursor.getInt(6)==1;
                String useName = cursor.getString(7);
                long timeLimit = Long.parseLong(cursor.getString(8));
                MessApp messApp = new MessApp(id,name,url,icon,countOpen,timeOpen,check,useName,timeLimit);
                if(isCheck&&check){
                    messApps.add(messApp);
                }else if(!isCheck&&!check) {
                    messApps.add(messApp);
                }

                if(id==2){
                    MessApp tiktok = getTikTok();
                    if(isCheck&&check){
                        messApps.add(tiktok);
                    }else if(!isCheck&&!check) {
                        messApps.add(tiktok);
                    }
                }

            }while (cursor.moveToNext());
            cursor.close();
        }


        Collections.sort(messApps, (s1, s2) -> {
            if (s1.getOpenCount() > s2.getOpenCount() )
                return -1;
            else if (s1.getOpenCount()  < s2.getOpenCount())
                return 1;
            return 0;
        });

        return messApps;
    }

    public ArrayList<MessApp> getAllMessApps(Context context){
        if(db==null)syn(context);
        ArrayList<MessApp> messApps = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM app",null);
        if(cursor!=null&&cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String icon = cursor.getString(2);
                String url = cursor.getString(3);
                long countOpen = Long.parseLong(cursor.getString(4));
                long timeOpen = Long.parseLong(cursor.getString(5));
                boolean check = cursor.getInt(6)==1;
                String useName = cursor.getString(7);
                long timeLimit = Long.parseLong(cursor.getString(8));
                MessApp messApp = new MessApp(id,name,url,icon,countOpen,timeOpen,check,useName,timeLimit);
                messApps.add(messApp);

                if(id==2){
                    MessApp tiktok = getTikTok();
                    messApps.add(tiktok);
                }

            }while (cursor.moveToNext());
            cursor.close();
        }

        Collections.sort(messApps, (s1, s2) -> {
            if (s1.getOpenCount() > s2.getOpenCount() )
                return -1;
            else if (s1.getOpenCount()  < s2.getOpenCount())
                return 1;
            return 0;
        });


        return messApps;
    }

    public ArrayList<MessApp> getAllMessAppsWithAdsType(Context context,int countShowAds){
        ArrayList<MessApp> messApps = getAllMessApps(context);
        ArrayList<MessApp> newApps = new ArrayList<>();
        for (int i=0;i<messApps.size();i++){
            if(i%countShowAds==0){
                newApps.add(new MessApp());
            }
            newApps.add(messApps.get(i));
        }
        MessApp messApp = new MessApp();
        messApp.setUrl("chart");
        newApps.add(0,messApp);
        return newApps;
    }

    public MessApp getMessApp(Context context,String name){

        if(name.equals(getTikTok().getName())){
            return getTikTok();
        }

        if(db==null)syn(context);
        Cursor cursor = db.rawQuery("SELECT * FROM app WHERE name == '"+name+"'",null);
        return getMessApp(cursor);
    }

    private MessApp getMessApp(Cursor cursor){
        if(cursor!=null&&cursor.moveToFirst()){
            int idd = cursor.getInt(0);
            String name = cursor.getString(1);
            String icon = cursor.getString(2);
            String url = cursor.getString(3);
            long countOpen = Long.parseLong(cursor.getString(4));
            long timeOpen = Long.parseLong(cursor.getString(5));
            boolean check = cursor.getInt(6)==1;
            String useName = cursor.getString(7);
            long timeLimit = Long.parseLong(cursor.getString(8));
            cursor.close();
            return new MessApp(idd,name,url,icon,countOpen,timeOpen,check,useName,timeLimit);
        }else {
            return null;
        }
    }


    public void syn(Context context) {
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

    public int addApp(Context context,String appname, String usename) {
        MessApp tiktok = getTikTok();
        if(appname.equals(tiktok.getName())){
            tiktok.setUsename(usename);
            setTikTok(tiktok);
            return 1;
        }

        if(db==null)syn(context);
        SQLiteStatement stmt = db.compileStatement("UPDATE app SET usename = '"+usename+"' , ischeck = 1 WHERE name == '"+appname+"'");
        int i = stmt.executeUpdateDelete();
        Log.e(TAG,i+"");
        return i;
    }
    public void launchOpenUp(Context context,MessApp messApp){

        MessApp tiktok = getTikTok();
        if(messApp.getName().equals(tiktok.getName())){
            setTikTok(messApp);
            return;
        }

        if(db==null)syn(context);
        SQLiteStatement stmt = db.compileStatement("UPDATE app SET countopen = '"+messApp.getOpenCount()+"' WHERE name == '"+messApp.getName()+"'");
        stmt.executeUpdateDelete();
    }
    public void updateTimeLimit(Context context,MessApp messApp){

        MessApp tiktok = getTikTok();
        if(messApp.getName().equals(tiktok.getName())){
            setTikTok(messApp);
            return;
        }

        if(db==null)syn(context);
        SQLiteStatement stmt = db.compileStatement("UPDATE app SET timelimit = '"+messApp.getTimeLimit()+"' WHERE name == '"+messApp.getName()+"'");
        stmt.executeUpdateDelete();
    }
    public int updateAppUserName(Context context,String appname, String usename) {

        MessApp tiktok = getTikTok();
        if(appname.equals(tiktok.getName())){
            tiktok.setUsename(usename);
            setTikTok(tiktok);
            return 1;
        }

        if(db==null)syn(context);
        SQLiteStatement stmt = db.compileStatement("UPDATE app SET usename = '"+usename+"' WHERE name == '"+appname+"'");
        int i = stmt.executeUpdateDelete();
        Log.e(TAG,i+"");
        return i;
    }

    public int deleteApp(Context context,String appname) {

        MessApp tiktok = getTikTok();
        if(appname.equals(tiktok.getName())){
            tiktok.setCheck(false);
            setTikTok(tiktok);
            return 1;
        }


        if(db==null)syn(context);
        SQLiteStatement stmt = db.compileStatement("UPDATE app SET  ischeck = 0 WHERE name == '"+appname+"'");
        int i = stmt.executeUpdateDelete();
        Log.e(TAG,i+"");
        return i;
    }
    public void close(){
        if(db!=null)
        db.close();
    }


}
