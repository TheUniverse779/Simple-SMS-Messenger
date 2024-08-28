package com.messenger.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class MyAds {

    public static void clearInterAds(){
        InterAds.clearFlag();
//        MyAdmobController.clearInter();
    }

    public static void initInterAds(Context context){
        InterAds.loadInter(context,null);
    }

    public static boolean isInterLoaded(){
        return InterAds.isCanShowAds();
    }

    public static void initBannerIds(Activity context){
        BannerAds.loadBannerAds(context);
//        MyAdmobController.initBannerAds(context);
    }


    public static void initBannerQuickAccess(View view){

//        MyAdmobController.initBannerQuickAccess(view);
    }

    public static void showInterFull(Activity context,Callback callback){
        InterAds.showAdsWithDialog(context,callback);
//        MyAdmobController.showAdsFullBeforeDoAction(context,callback);
    }

    public static void showInterFullNow(Activity context,Callback callback){
        InterAds.clearFlag();
        InterAds.showAdsWithDialog(context,callback);
    }

    public static int getFlag() {
        return InterAds.flagQC;
    }


    public static void initNativeMain(View view){
        NativeLoadAds.loadNativeAndShow(view,false);
//        MyAdmobController.initNativeMain(view);
    }

    public static void initNativeList(View view){

        NativeLoadAds.loadNativeAndShow(view,true);
    }
}
