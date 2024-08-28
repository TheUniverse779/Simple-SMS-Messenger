package com.messenger.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.messenger.App;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.ads.GDPRConsentHelper;
import com.messenger.ads.MyAds;
import com.messenger.ads.OpenAds;
import com.messenger.helper.LockDatabase;
import com.messenger.views.UnlockDialog;

public class SplashActivity extends AppCompatActivity {

    private int delay = 1000;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch(Exception ignore){}
        setContentView(R.layout.activity_splash);

        if(!LockDatabase.get().isLockAppEnableEmpty()){
            App.get().startLockAppService();
        }



        App.get().sendTracker(getClass().getSimpleName());

        if(LockDatabase.get().isDeviceAppEmpty()){
            LockDatabase.get().synDb();
        }

        GDPRConsentHelper.get().syn(this, this::setup);

    }

    private void setup() {
        if(!MyAds.isInterLoaded()){
            OpenAds.initOpenAds(this, (value, where) -> {

            });
            MyAds.initInterAds(this);
            delay = 4000;
        }

        new UnlockDialog(this) {
            @Override
            public void onSuccess() {
                if(!MyAds.isInterLoaded()){
                    MyAds.initInterAds(SplashActivity.this);
                    delay = 4000;
                }

                new Handler().postDelayed(() -> {
                    if(isRunning){
                        MyAds.showInterFullNow(SplashActivity.this, (value1, where1) -> {
                            startMain();
                        });
                    }else {
                        startMain();
                    }
                },delay);
            }
        }.show();
    }


    private void startMain(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

}
