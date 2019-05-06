package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
public class CourseHour {
    @PrimaryKey(autoGenerate = true)
    private long courseHourID;
    @ColumnInfo
    private Course course;
    @ColumnInfo
    private Calendar startDate;
    @ColumnInfo
    private Calendar endDate;
    @Ignore
    private List<Note> notes;
    @Ignore
    private List<Photo> photos;

    public CourseHour(Course course, Calendar startDate, Calendar endDate) {
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        notes = new ArrayList<>();
        photos = new ArrayList<>();
    }

    public long getCourseHourID() {
        return courseHourID;
    }

    public void setCourseHourID(long courseHourID) {
        this.courseHourID = courseHourID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Context context, Note note) {
        notes.add(note);
        CourseDiaryDB.getDBInstance(context).noteDAO().addNote(note);
    }

    public void deleteNote(Context context, Note note) {
        notes.remove(note);
        CourseDiaryDB.getDBInstance(context).noteDAO().deleteNote(note);
    }
}