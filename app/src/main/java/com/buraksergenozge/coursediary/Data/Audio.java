package com.buraksergenozge.coursediary.Data;

import android.provider.MediaStore;

import java.util.Calendar;

public class Audio {
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
}