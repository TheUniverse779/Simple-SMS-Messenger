package com.messenger.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.messenger.App;
import com.simplemobiletools.smsmessenger.BuildConfig;
import com.simplemobiletools.smsmessenger.R;

import java.util.Date;

public class InterAds {

    private static final String INTER_TEST_ID = "ca-app-pub-3940256099942544/1033173712";

    private static final String INTER_ID = "ca-app-pub-7057107138215897/4612370802";

    private static InterstitialAd mInterstitialAd;
    private static boolean isLoading = false;
    private static boolean isShowing = false;
    private static long loadTimeAd = 0;

    public static int flagQC = 1;

    public static boolean isShowing() {
        return isShowing;
    }

    public static void loadInter(final Context ac, Callback callback){

        if(isCanLoadAds()){
            mInterstitialAd = null;
            isLoading = true;
            InterstitialAd.load(ac, BuildConfig.DEBUG ? INTER_TEST_ID : INTER_ID, getAdRequest(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAd = interstitialAd;
                    isLoading = false;
                    loadTimeAd = (new Date()).getTime();
                    if(callback!=null){
                        callback.callBack(0,0);
                    }
                    interstitialAd.setOnPaidEventListener(adValue -> {
                        try {
                            App.get().initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                            adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                            Adjust.trackAdRevenue(adRevenue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    mInterstitialAd = null;
                    isLoading = false;
                    if(callback!=null){
                        callback.callBack(0,0);
                    }
                }
            });

        }else {
            if(callback!=null){
                callback.callBack(0,0);
            }
        }
    }
    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private static boolean isCanLoadAds() {
        if(isLoading){
            return false;
        }
        if(isShowing){
            return false;
        }
        if(mInterstitialAd==null){
            return true;
        }else {
            return isAdsOverdue();
        }
    }

    public static boolean isCanShowAds(){
        if(isLoading){
            return false;
        }
        if(isShowing){
            return false;
        }
        if(flagQC==0){
            return false;
        }
        if(mInterstitialAd==null) {
            return false;
        }else {
            return !isAdsOverdue();
        }

    }

    private static boolean isAdsOverdue(){
        long dateDifference = (new Date()).getTime() - loadTimeAd;
        long numMilliSecondsPerHour = 3600000;
        return dateDifference > (numMilliSecondsPerHour * (long) 4);
    }


    public static void showAdsWithDialog(Activity activity, Callback callback){
        if(isCanShowAds()){
            showAds(activity,callback);
        }else {
            callback.callBack(0,0);
        }
    }

    private static void showAds(Activity context, Callback callback){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                mInterstitialAd = null;
                isShowing = false;
                if(callback!=null){
                    callback.callBack(0,0);
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                isShowing = true;
            }
            @Override
            public void onAdDismissedFullScreenContent() {
                context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                super.onAdDismissedFullScreenContent();
                isShowing = false;
                mInterstitialAd = null;
                startDelay();
                loadInter(context,null);
                if(callback!=null){
                    callback.callBack(0,0);
                }
            }

        });
        mInterstitialAd.show(context);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public static void startDelay(){
        flagQC = 0;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,45000);
    }


    public static void clearFlag() {
        flagQC = 1;
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
    }

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private  static final Runnable runnable = () -> flagQC = 1;
}
