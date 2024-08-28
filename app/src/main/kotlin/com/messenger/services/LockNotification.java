package com.messenger.services;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.simplemobiletools.smsmessenger.R;
import com.messenger.activities.MainActivity;

public class LockNotification {

    @RequiresApi(api = Build.VERSION_CODES.M)
    static void createNotification(Service context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Object systemService = context.getSystemService(NOTIFICATION_SERVICE);
        if (systemService != null) {
            NotificationManager notificationManager = (NotificationManager) systemService;
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("10001", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_MIN);
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
                notificationManager.createNotificationChannel(notificationChannel);

            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(),"10001");
            builder.setSmallIcon(R.drawable.ic_home)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setChannelId("10001")
                    .setPriority(Notification.PRIORITY_MIN)
                    .setContentIntent(activity);
            if(Build.VERSION.SDK_INT >= 26){
                builder.setContentTitle(context.getString(R.string.app_name));
                builder.setContentText("Open all messenger apps in one place");
            }else {
                builder.setContentText(context.getString(R.string.app_name));
                builder.setContentTitle(context.getString(R.string.app_name));
            }

            context.startForeground(1,builder.build());
        }
    }

}
