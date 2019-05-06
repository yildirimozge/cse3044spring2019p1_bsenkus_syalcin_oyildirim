package com.buraksergenozge.coursediary.Data;

import android.media.Image;

import java.util.Calendar;

public class Photo extends AppContent {
    private CourseHour courseHour;
    private Calendar createDate;
    private Image image;

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}