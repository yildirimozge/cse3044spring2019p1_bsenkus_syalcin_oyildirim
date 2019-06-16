package com.buraksergenozge.coursediary.Data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Tools.AlarmService;

import java.util.Calendar;

public abstract class AppContent {
    public static void openCreationDialog(AppCompatActivity activity, CreationDialog dialog) {
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }

    protected abstract int getSaveMessage();

    protected abstract void addOperation(AppCompatActivity activity);

    public void create(AppCompatActivity activity) {
        addOperation(activity);
        ((MainScreen)activity).updateViewsOfAppContent(this);
        MainScreen.showSnackbarMessage(activity.getWindow().getDecorView(), activity.getString(getSaveMessage()));
    }

    protected abstract int getDeleteMessage();

    protected abstract void deleteOperation(AppCompatActivity activity);

    public void remove(AppCompatActivity activity) {
        deleteOperation(activity);
        ((MainScreen)activity).updateViewsOfAppContent(this);
        MainScreen.showSnackbarMessage(activity.getWindow().getDecorView(), activity.getString(getDeleteMessage()));
    }

    public abstract void edit(AppCompatActivity activity);

    public abstract void updateOperation(AppCompatActivity activity);

    public abstract void showInfo(AppCompatActivity activity);

    public abstract void fillSpinners(CreationDialog creationDialog);

    public abstract String[] getRelatedFragmentTags();

    public static long getTimeDifferenceInMillis(Calendar start, Calendar end) {
        if (end.getTimeInMillis() - start.getTimeInMillis() < 0)
            return 0;
        return end.getTimeInMillis() - start.getTimeInMillis();
    }

    void setOrCancelNotification(AppCompatActivity activity, int requestCode, Calendar notificationTime, String title, String message, boolean cancel) {
        Intent serviceIntent = new Intent(activity, AlarmService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("message", message);
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        // make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
        PendingIntent servicePendingIntent = PendingIntent.getService(activity, requestCode, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
        if (cancel)
            alarmManager.cancel(servicePendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), servicePendingIntent);
    }
}