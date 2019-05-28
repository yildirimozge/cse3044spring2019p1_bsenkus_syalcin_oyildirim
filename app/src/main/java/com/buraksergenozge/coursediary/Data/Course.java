package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;

import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Semester.class, parentColumns = "semesterID", childColumns = "semester", onDelete = CASCADE))
public class Course extends AppContent{
    @PrimaryKey(autoGenerate = true)
    private long courseID;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private Semester semester;
    @ColumnInfo
    private int credit;
    @ColumnInfo
    private float attendanceObligation;
    @ColumnInfo
    private List<Calendar[]> schedule; // List of start and end hours of course
    @ColumnInfo
    private GradingSystem gradingSystem;
    @ColumnInfo
    private Grade grade;
    @Ignore
    private List<CourseHour> courseHours;
    @Ignore
    private List<Assignment> assignments;
    @Ignore
    private static DialogFragment creationDialog = new CourseCreationDialog();

    public Course(String name, Semester semester, int credit, float attendanceObligation, List<Calendar[]> schedule) {
        this.name = name;
        this.semester = semester;
        this.credit = credit;
        this.attendanceObligation = attendanceObligation;
        this.schedule = schedule;
        this.gradingSystem = null;
        this.grade = null;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public float getAttendanceObligation() {
        return attendanceObligation;
    }

    public void setAttendanceObligation(float attendanceObligation) {
        this.attendanceObligation = attendanceObligation;
    }

    public List<Calendar[]> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Calendar[]> schedule) {
        this.schedule = schedule;
    }

    public GradingSystem getGradingSystem() {
        return gradingSystem;
    }

    public void setGradingSystem(GradingSystem gradingSystem) {
        this.gradingSystem = gradingSystem;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
        semester.updateGPA();
    }

    public List<CourseHour> getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(List<CourseHour> courseHours) {
        this.courseHours = courseHours;
    }

    public void schedule() {
        for (Calendar[] entry : schedule) {
            CourseHour courseHour;
            Calendar startTime = entry[0];
            Calendar endTime = entry[1];
            int dayDifference = startTime.get(Calendar.DAY_OF_WEEK) - semester.getStartDate().get(Calendar.DAY_OF_WEEK);
            Calendar currentDate = (Calendar)semester.getStartDate().clone();
            if(dayDifference < 0)
                dayDifference += 7;
            currentDate.add(Calendar.DATE, dayDifference);
            while(semester.getEndDate().after(currentDate)) {
                Calendar start = (Calendar) Calendar.getInstance().clone();
                Calendar end = (Calendar) Calendar.getInstance().clone();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);
                start.set(year, month, day, startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE));
                start.set(year, month, day, endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE));
                courseHour = new CourseHour(this, start, end);
                courseHours.add(courseHour);
                currentDate.add(Calendar.DATE, 7);
            }
        }
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public static DialogFragment getCreationDialog() {
        return creationDialog;
    }

    public static void setCreationDialog(DialogFragment creationDialog) {
        Course.creationDialog = creationDialog;
    }

    public void addAssignment(Context context, Assignment assignment) {
        assignments.add(assignment);
        CourseDiaryDB.getDBInstance(context).assignmentDAO().addAssignment(assignment);
    }

    public void deleteAssignment(Context context, Assignment assignment) {
        assignments.remove(assignment);
        CourseDiaryDB.getDBInstance(context).assignmentDAO().deleteAssignment(assignment);
    }

    public void integrateWithDB(Context context) {
        courseHours = CourseDiaryDB.getDBInstance(context).courseDAO().getAllCourseHoursOfCourse(this);
        assignments = CourseDiaryDB.getDBInstance(context).courseDAO().getAllAssignmentsOfCourse(this);
    }

    @Override
    public String toString() {
        return name;
    }
}