package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Semester.class, parentColumns = "semesterID", childColumns = "semester", onDelete = CASCADE))
public class Course extends AppContent{
    @PrimaryKey(autoGenerate = true)
    private long courseID;
    @ColumnInfo
    private Semester semester;
    @ColumnInfo
    private int credit;
    @ColumnInfo
    private float attendanceObligation;
    @ColumnInfo
    private Map<Integer, Calendar[]> schedule; // Integer represents the day of week, and array is for starting and end times of course
    @ColumnInfo
    private GradingSystem gradingSystem;
    @ColumnInfo
    private Grade grade;
    @Ignore
    private List<CourseHour> courseHours;
    @Ignore
    private List<Assignment> assignments;
    @Ignore
    private static DialogFragment creationDialog;

    public Course(Semester semester, float attendanceObligation, Map<Integer, Calendar[]> schedule) {
        this.semester = semester;
        this.attendanceObligation = attendanceObligation;
        this.schedule = schedule;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
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

    public Map<Integer, Calendar[]> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<Integer, Calendar[]> schedule) {
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
        for (Map.Entry<Integer, Calendar[]> entry : schedule.entrySet()) {
            CourseHour courseHour;
            int dayDifference = entry.getKey() - semester.getStartDate().get(Calendar.DAY_OF_WEEK);
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
                Calendar[] clocks = entry.getValue();
                start.set(year, month, day, clocks[0].get(Calendar.HOUR_OF_DAY), clocks[0].get(Calendar.MINUTE));
                start.set(year, month, day, clocks[1].get(Calendar.HOUR_OF_DAY), clocks[1].get(Calendar.MINUTE));
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

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void deleteAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }
}