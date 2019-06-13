package com.buraksergenozge.coursediary.Data;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;

import java.util.Calendar;
import java.util.Objects;

public class Audio extends AppContent{
    private CourseHour courseHour;
    private MediaStore.Audio record;
    private Calendar createDate;

    public Audio(CourseHour courseHour, String text) {
        this.courseHour = courseHour;
        this.record = null;
        createDate = (Calendar)Calendar.getInstance().clone();
    }

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public MediaStore.Audio getRecord() {
        return record;
    }

    public void setRecord(MediaStore.Audio record) {
        this.record = record;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    @Override
    public int getSaveMessage() {
        return 0;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {

    }

    @Override
    public int getDeleteMessage() {
        return 0;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {

    }

    private static CreationDialog getCreationDialog(int mode) {
        //CreationDialog creationDialog = new AssignmentCreationDialog();
        //creationDialog.isEditMode = isEditMode;
        return null;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, Objects.requireNonNull(getCreationDialog(CreationDialog.EDIT_MODE)));
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {

    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {

    }

    @Override
    public void showInfo(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, Objects.requireNonNull(getCreationDialog(CreationDialog.INFO_MODE)));
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return new String[0];
    }
}