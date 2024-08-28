package com.messenger.ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdActivity;
import com.messenger.App;
import com.messenger.activities.MainActivity;
import com.messenger.activities.SplashActivity;
import com.messenger.activities.UnlockAppActivity;
import com.messenger.utils.Constant;
import com.messenger.utils.PreferenceUtil;


public class OpenAdsHelper implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private MainActivity mainActivity;
    private Activity moreActivity;
    private Activity currentActivity;

    public static boolean isGoSetting = false;

    /**
     * LifecycleObserver methods
     */

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        if(PreferenceUtil.getInstance(App.get()).getValue(Constant.SharePrefKey.SHOW_OPEN_ADS, "no").equals("yes")) {
            try {
                if (mainActivity != null) {
                    //            if (!mainActivity.hasStoragePermission()) {
                    //                return;
                    //            }
                    if (moreActivity != null) {
                        return;
                    }
                    if(currentActivity != null ){
                        if (OpenAds.isCanShowOpenAds() && !InterAds.isShowing()) {
                            OpenAds.showOpenAds(mainActivity, (value, where) -> {

                            });
                        }
                    }else {
                        if (OpenAds.isCanShowOpenAds() && !InterAds.isShowing()) {
                            OpenAds.showOpenAds(mainActivity, (value, where) -> {
                            });
                        }
                    }
                } else if (currentActivity != null) {
                    if (moreActivity != null) {
                        return;
                    }
                    if (OpenAds.isCanShowOpenAds() && !InterAds.isShowing()) {
                        if (currentActivity instanceof SplashActivity || currentActivity instanceof UnlockAppActivity) {
                            // not show
                        } else {
                            OpenAds.showOpenAds(currentActivity, (value, where) -> {
                            });
                        }
                    }
                }
            } catch (Exception e) {

            }
        }

    }

    public void setup(Application application) {
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        } else if (activity instanceof AdActivity) {
            moreActivity = activity;
        } else currentActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        } else if (activity instanceof AdActivity) {
            moreActivity = activity;
        } else currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        } else if (activity instanceof AdActivity) {
            moreActivity = activity;
        } else currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle
            outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (activity instanceof MainActivity) {
            mainActivity = null;
        } else if (activity instanceof AdActivity) {
            moreActivity = null;
        } else {
            currentActivity = null;
        }
    }

}
