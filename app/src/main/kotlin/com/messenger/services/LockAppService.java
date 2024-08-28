package com.messenger.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.messenger.activities.UnlockAppActivity;
import com.messenger.helper.LockDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class LockAppService extends Service {

    private static final String TAG = LockAppService.class.getSimpleName();

    public static final String KEY_APP_ID = "app_id";

    private String appCurrent="";

    private Timer mTimer;

    private TimerTask mTimerTask;

    private Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LockNotification.createNotification(this);
        }

        Log.e(TAG, "onStartCommand");

        startRun();
        mHandler = new Handler();



        return START_STICKY;
    }

    private void startRun() {
        try {
            resetAll();
            scheduleShowAds();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void resetAll() {
        try {
            if (mTimerTask != null)
                mTimerTask.cancel();
            mTimerTask = null;
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            if (mTimer != null)
                mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void scheduleShowAds() {
        mTimer = new Timer();
        if (mTimerTask != null)
            mTimerTask.cancel();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    checkRunningApps();
                } catch (Exception ignored){}

            }
        };
        mTimer.schedule(mTimerTask, 0L, 500L);
    }



    private void checkRunningApps() {

        appCurrent = LockDatabase.get().getAppCurrent(appCurrent);


        if(!appCurrent.contains(getPackageName())&&
                !appCurrent.contains("com.google.android.packageinstaller")&&
                !appCurrent.contains("com.google.android.gsf")&&
                !appCurrent.equals(LockDatabase.get().getAppCurrent())){

            Log.e(TAG, appCurrent);

            LockDatabase.get().setAppCurrent(appCurrent);

            if(LockDatabase.get().isAppEnableLock()){
                mHandler.post(() -> {
                    Intent lockIntent = new Intent(LockAppService.this, UnlockAppActivity.class);
                    lockIntent.putExtra(KEY_APP_ID,appCurrent);
                    lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_NO_USER_ACTION
                            | Intent.FLAG_ACTIVITY_NO_ANIMATION
                            | Intent.FLAG_FROM_BACKGROUND);
                    getApplicationContext().startActivity(lockIntent);
                });
            }

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), LockAppService.class);
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT| PendingIntent.FLAG_IMMUTABLE);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 500, pendingIntent);
        super.onTaskRemoved(rootIntent);

    }

}
