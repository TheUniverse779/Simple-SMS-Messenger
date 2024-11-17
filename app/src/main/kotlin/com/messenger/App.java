package com.messenger;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.LogLevel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.messenger.ads.OpenAdsHelper;
import com.messenger.services.LockAppService;
import com.messenger.utils.Constant;
import com.messenger.utils.PreferenceUtil;
import com.messenger.utils.Utils;
import com.simplemobiletools.smsmessenger.R;

public class App extends Application {

    private static App app;

    public static App get() {
        return app;
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        new OpenAdsHelper().setup(this);
        initRemoteConfig();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        String appToken = "v5rlk2sd57uo";
        AdjustConfig config = new AdjustConfig(this,
                appToken,AdjustConfig.ENVIRONMENT_PRODUCTION);
        config.setLogLevel(LogLevel.ERROR);
        Adjust.onCreate(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

    }

    public void initROAS(double revenue, String currency) {
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            double currentImpressionRevenue = revenue / 1000000; //LTV pingback provides value in micros, so if you are using that directly,
            // make sure to divide by 10^6
            float previousTroasCache = sharedPref.getFloat("TroasCache", 0); //Use App Local storage to store cache of tROAS
            float currentTroasCache = (float) (previousTroasCache + currentImpressionRevenue);
            //check whether to trigger  tROAS event
            if (currentTroasCache >= 0.01) {
                LogTroasFirebaseAdRevenueEvent(currentTroasCache, currency);
                editor.putFloat("TroasCache", 0);//reset TroasCache
            } else {
                editor.putFloat("TroasCache", currentTroasCache);
            }
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LogTroasFirebaseAdRevenueEvent(float tRoasCache, String currency) {
        try {
            Bundle bundle = new Bundle();
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, tRoasCache);//(Required)tROAS event must include Double Value
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);//put in the correct currency
            mFirebaseAnalytics.logEvent("Daily_Ads_Revenue", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logEvent(String name){
        mFirebaseAnalytics.logEvent(name,new Bundle());
    }


    public void startLockAppService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(Utils.checkUsagePermission(this)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, LockAppService.class));
                }else {
                    startService(new Intent(this, LockAppService.class));
                }
            }
        }
    }

    public void stopLockAppService() {
        stopService(new Intent(this, LockAppService.class));
    }

    public void sendTracker(String screenName){
//        FlurryAgent.logEvent(screenName);

        logEvent(screenName);
    }


    private void initRemoteConfig() {
        try {
            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener( new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {
                    fetchConfig();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchConfig() {
        try {
            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            String native_ads = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.NATIVE_ADS);
            String show_open_ads = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.SHOW_OPEN_ADS);
            String banner_coll = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.BANNER_COLL);
            String show_banner_coll = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.SHOW_BANNER_COLL);
            String game1 = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.GAME1);
            String game2 = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.GAME2);
            String game = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.GAME);
            String sms_ads = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.SMS_ADS);
//            String test_clock = "yes";
            Log.e("heheheheheh", show_banner_coll);
            //CHƯA TEST
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.NATIVE_ADS, native_ads);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.SHOW_OPEN_ADS, show_open_ads);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.BANNER_COLL, banner_coll);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.SHOW_BANNER_COLL, show_banner_coll);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.GAME1, game1);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.GAME2, game2);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.GAME, game);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.SMS_ADS, sms_ads);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }


}
