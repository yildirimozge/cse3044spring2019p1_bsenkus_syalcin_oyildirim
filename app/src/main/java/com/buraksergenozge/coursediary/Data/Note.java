package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = CourseHour.class, parentColumns = "courseHourID", childColumns = "courseHour", onDelete = CASCADE))
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long noteID;
    @ColumnInfo
    private CourseHour courseHour;
    @ColumnInfo
    private String text;
    @ColumnInfo
    private Calendar createDate;

    public Note(CourseHour courseHour, String text) {
        this.courseHour = courseHour;
        this.text = text;
        createDate = (Calendar)Calendar.getInstance().clone();
    }

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public long getNoteID() {
        return noteID;
    }

    public void setNoteID(long noteID) {
        this.noteID = noteID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }
}