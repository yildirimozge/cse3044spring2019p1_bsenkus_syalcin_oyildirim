package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "courseID", childColumns = "course", onDelete = CASCADE))
public class CourseHour implements Comparable<CourseHour>{
    @PrimaryKey(autoGenerate = true)
    private long courseHourID;
    @ColumnInfo
    private Course course;
    @ColumnInfo
    private Calendar startDate;
    @ColumnInfo
    private Calendar endDate;
    @Ignore
    private List<Note> notes = new ArrayList<>();
    @Ignore
    private List<Photo> photos = new ArrayList<>();

    public CourseHour(Course course, Calendar startDate, Calendar endDate) {
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
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
        long noteID = CourseDiaryDB.getDBInstance(context).noteDAO().addNote(note);
        note.setNoteID(noteID);
        notes.add(note);
    }

    public void deleteNote(Context context, Note note) {
        notes.remove(note);
        CourseDiaryDB.getDBInstance(context).noteDAO().deleteNote(note);
    }

    @Override
    public String toString() {
        String repr;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM EEE");
        SimpleDateFormat clockFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(startDate.getTimeZone());
        clockFormat.setTimeZone(startDate.getTimeZone());
        if (startDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH))
            repr = dateFormat.format(startDate.getTime()) + " (" + clockFormat.format(startDate.getTime()) + " - " + clockFormat.format(endDate.getTime()) + ")";
        else
            repr = dateFormat.format(startDate.getTime()) + " " + clockFormat.format(startDate.getTime()) + " - " + dateFormat.format(endDate.getTime()) + " " + clockFormat.format(endDate.getTime());
        return repr;
    }

    @Override
    public int compareTo(CourseHour courseHour) {
        return (int)(startDate.getTimeInMillis() - courseHour.startDate.getTimeInMillis());
    }
}
