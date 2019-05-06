package com.buraksergenozge.coursediary.Data;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

public abstract class AppContent {
    public static void openCreationDialog(AppCompatActivity activity, DialogFragment dialog) {
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }
}
