package com.buraksergenozge.coursediary.Data;

import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;

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
}