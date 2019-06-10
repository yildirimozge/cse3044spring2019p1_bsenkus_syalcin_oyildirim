package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Fragments.ArchiveFragment;
import com.buraksergenozge.coursediary.Fragments.AssignmentFragment;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.SemesterCreationDialog;
import com.buraksergenozge.coursediary.R;

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
    private static String[] relatedFragmentTags = {ArchiveFragment.tag, CourseFeed.tag, CourseHourFragment.tag, AssignmentFragment.tag};

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

    void updateGPA() {
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

    public static DialogFragment getCreationDialog(boolean isEditMode) {
        CreationDialog creationDialog = new SemesterCreationDialog();
        creationDialog.isEditMode = isEditMode;
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(true));
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

    public boolean getCourseHoursEmpty() {
        for (Course course: courses) {
            if (course.getCourseHours().size() > 0)
                return false;
        }
        return true;
    }

    @Override
    public int getSaveMessage() {
        return R.string.semester_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.semesterID = CourseDiaryDB.getDBInstance(activity).semesterDAO().addSemester(this);
        User.getSemesters().add(this);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.semester_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        User.getSemesters().remove(this);
        CourseDiaryDB.getDBInstance(activity).semesterDAO().deleteSemester(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).semesterDAO().update(this);
    }

    @Override
    public void showInfo(final AppCompatActivity activity) {
        Toast.makeText(activity, toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
        creationDialog.selectSemesterOnSpinner(this);
        if (!(creationDialog instanceof CourseCreationDialog))
            return;
        creationDialog.selectedSemester = (Semester)creationDialog.semesterSelectionSpinner.getSelectedItem();
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    @NonNull
    public String toString() {
        return name;
    }
}