package com.buraksergenozge.coursediary.Tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class AlarmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String CHANNEL_ID = "CourseDiary";
        String CHANNEL_NAME = "Course Diary";
        Bundle extras = intent.getExtras();
        String title, message;
        if (extras != null) {
            title = Objects.requireNonNull(extras.get("title")).toString();
            message = Objects.requireNonNull(extras.get("message")).toString();
        }
        else {
            title = getString(R.string.notification_assignment_title);
            message = "Yaklaşan ödev teslimin var!";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            int NOTIFICATION_ID = 52; //Notification id si, channel ile ilgisi bulunmuyor
            Notification notification = new Notification.Builder(getBaseContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_assignment_green_36dp)
                    .setAutoCancel(true)
                    .build();
            manager.notify(NOTIFICATION_ID, notification);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}