package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.app.DialogFragment;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;

import java.util.Calendar;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "courseID", childColumns = "course", onDelete = CASCADE))
public class Assignment extends AppContent {
    @PrimaryKey(autoGenerate = true)
    private long assignmentID;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private Course course;
    @ColumnInfo
    private Calendar deadline;
    @Ignore
    private static DialogFragment creationDialog = new AssignmentCreationDialog();

    public Assignment(String title, Course course, Calendar deadline) {
        this.title = title;
        this.course = course;
        this.deadline = deadline;
    }

    public long getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(long assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public static DialogFragment getCreationDialog() {
        return creationDialog;
    }

    public static void setCreationDialog(DialogFragment creationDialog) {
        Assignment.creationDialog = creationDialog;
    }

    @Override
    public String toString() {
        return title;
    }
}