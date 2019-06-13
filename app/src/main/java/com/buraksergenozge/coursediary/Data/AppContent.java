package com.buraksergenozge.coursediary.Data;

import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;

public abstract class AppContent {
    public static void openCreationDialog(AppCompatActivity activity, CreationDialog dialog) {
        dialog.show(activity.getSupportFragmentManager(), "tag");
    }

    public abstract int getSaveMessage();

    public abstract void addOperation(AppCompatActivity activity);

    public abstract int getDeleteMessage();

    public abstract void deleteOperation(AppCompatActivity activity);

    public abstract void edit(AppCompatActivity activity);

    public abstract void updateOperation(AppCompatActivity activity);

    public abstract void showInfo(AppCompatActivity activity);

    public abstract void fillSpinners(CreationDialog creationDialog);

    public abstract String[] getRelatedFragmentTags();
}