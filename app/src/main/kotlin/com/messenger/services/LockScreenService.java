package com.messenger.services;//package com.messenger.services;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.SystemClock;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import com.messenger.activities.LockScreenActivity;
//import com.messenger.helper.SqDatabase;
//
//public class LockScreenService extends Service {
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private BroadcastReceiver  mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction()+"";
//            Log.e("hoang",action+"/"+SqDatabase.getUser().isLockScreen(context));
//            if(action.equals(Intent.ACTION_SCREEN_OFF)
//                    && SqDatabase.getUser().isLockScreen(context)){
//                Intent lockIntent = new Intent(context, LockScreenActivity.class);
//                lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                        | Intent.FLAG_ACTIVITY_NO_USER_ACTION
//                        | Intent.FLAG_ACTIVITY_NO_ANIMATION
//                        | Intent.FLAG_FROM_BACKGROUND);
//                context.getApplicationContext().startActivity(lockIntent);
//            }
//        }
//    };;
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        try {
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Intent.ACTION_SCREEN_OFF);
//            registerReceiver(mReceiver, filter);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LockNotification.createNotification(this);
//        }
//
//        return START_STICKY;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        try {
//            unregisterReceiver(mReceiver);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//
//        Intent restartServiceIntent = new Intent(getApplicationContext(),
//                this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//
//        PendingIntent restartServicePendingIntent = PendingIntent.getService(
//                getApplicationContext(), 1, restartServiceIntent,
//                PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext()
//                .getSystemService(Context.ALARM_SERVICE);
//        if (alarmService != null) {
//            alarmService.set(AlarmManager.ELAPSED_REALTIME,
//                    SystemClock.elapsedRealtime() + 1000,
//                    restartServicePendingIntent);
//        }
//
//        super.onTaskRemoved(rootIntent);
//
//    }
//
//}
