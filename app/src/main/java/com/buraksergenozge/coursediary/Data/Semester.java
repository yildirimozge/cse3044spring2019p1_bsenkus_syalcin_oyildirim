package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.SemesterCreationDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity
public class Semester extends AppContent {
    @PrimaryKey(autoGenerate = true)
    private long semesterID;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private Calendar startDate;
    @ColumnInfo
    private Calendar endDate;
    @Ignore
    private List<Course> courses = new ArrayList<>();
    @ColumnInfo
    private float gpa;
    @Ignore
    private static DialogFragment creationDialog = new SemesterCreationDialog();

    public Semester(String name, Calendar startDate, Calendar endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(long semesterID) {
        this.semesterID = semesterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public void updateGPA() {
        float point = 0;
        int totalCredit = 0;
        for (Course course: courses) {
            point = course.getCredit() * course.getGrade().getCoefficient();
            totalCredit += course.getCredit();
        }
        if (totalCredit == 0)
            gpa = 0.0f;
        else
            gpa = point / totalCredit;
    }

    public static DialogFragment getCreationDialog() {
        return creationDialog;
    }

    public String getStartDateString() {
        return startDate.get(Calendar.YEAR) + "." + (startDate.get(Calendar.MONTH) + 1) + "." + startDate.get(Calendar.DAY_OF_MONTH);
    }

    public String getEndDateString() {
        return endDate.get(Calendar.YEAR) + "." + (endDate.get(Calendar.MONTH) + 1) + "." + endDate.get(Calendar.DAY_OF_MONTH);
    }

    public void addCourse(Context context, Course course) {
        long courseID = CourseDiaryDB.getDBInstance(context).courseDAO().addCourse(course);
        course.setCourseID(courseID);
        courses.add(course);

    }

    public void deleteCourse(Context context, Course course) {
        courses.remove(course);
        CourseDiaryDB.getDBInstance(context).courseDAO().deleteCourse(course);
    }

    public void update(Context context) {
        CourseDiaryDB.getDBInstance(context).semesterDAO().update(this);
    }

    public List<Course> integrateWithDB(Context context) {
        courses = CourseDiaryDB.getDBInstance(context).semesterDAO().getAllCoursesOfSemester(this);
        return courses;
    }

    public long getNumberOfDaysRemaining() {
        Calendar now = Calendar.getInstance();
        long end = endDate.getTimeInMillis();
        long start = now.getTimeInMillis();
        long difference = end - start;
        if (difference < 86400000)
            return 0;
        return TimeUnit.MILLISECONDS.toDays(Math.abs(difference));
    }

    public String toString() {
        return name;
    }
}
