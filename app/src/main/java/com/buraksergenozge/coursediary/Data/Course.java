package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.AssignmentFragment;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.CourseFragment;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.NoteCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.PhotoCreationDialog;
import com.buraksergenozge.coursediary.Fragments.SemesterFragment;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Semester.class, parentColumns = "semesterID", childColumns = "semester", onDelete = CASCADE),
        indices = {@Index("semester")})
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
    private List<CourseHour> courseHours = new ArrayList<>();
    @Ignore
    private List<Assignment> assignments = new ArrayList<>();
    @Ignore
    private static String[] relatedFragmentTags = {SemesterFragment.tag, CourseFragment.tag, CourseFeed.tag, CourseHourFragment.tag, AssignmentFragment.tag};

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
    }

    public List<CourseHour> getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(List<CourseHour> courseHours) {
        this.courseHours = courseHours;
    }

    private void schedule(AppCompatActivity activity) {
        for (Calendar[] entry : schedule) {
            CourseHour courseHour;
            Calendar startTime = entry[0];
            Calendar endTime = entry[1];
            int dayDifference = startTime.get(Calendar.DAY_OF_WEEK) - semester.getStartDate().get(Calendar.DAY_OF_WEEK);
            if(dayDifference < 0)
                dayDifference += 7;
            Calendar currentStart = (Calendar)semester.getStartDate().clone();
            currentStart.add(Calendar.DATE, dayDifference);
            currentStart.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
            currentStart.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));

            Calendar currentEnd = (Calendar) currentStart.clone();
            currentEnd.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
            currentEnd.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
            int dayDifferenceOfStartAndEnd = startTime.get(Calendar.DAY_OF_WEEK) - endTime.get(Calendar.DAY_OF_WEEK);
            if(dayDifferenceOfStartAndEnd < 0)
                dayDifferenceOfStartAndEnd += 7;
            currentEnd.add(Calendar.DATE, dayDifferenceOfStartAndEnd);

            while(semester.getEndDate().after(currentStart)) {
                Calendar start = (Calendar) currentStart.clone();
                Calendar end = (Calendar) currentEnd.clone();
                courseHour = new CourseHour(this, start, end);
                courseHour.addOperation(activity);
                currentStart.add(Calendar.DATE, 7);
                currentEnd.add(Calendar.DATE, 7);
            }
        }
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public static CreationDialog getCreationDialog(int mode) {
        CreationDialog creationDialog = new CourseCreationDialog();
        creationDialog.mode = mode;
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.EDIT_MODE));
    }

    public float getAttendanceStatus() {
        int numberOfCourseHours = 0;
        int totalAttendance = 0;
        for (CourseHour courseHour: courseHours) {
            if (courseHour.getCancelled() == 0) {
                numberOfCourseHours++;
                if (courseHour.getAttendance() == 1)
                    totalAttendance++;
            }
        }
        if (numberOfCourseHours == 0 || totalAttendance == 0)
            return 0;
        return ((float) totalAttendance / numberOfCourseHours) * 100;
    }

    public void integrateWithDB(Context context) {
        courseHours = CourseDiaryDB.getDBInstance(context).courseDAO().getAllCourseHoursOfCourse(this);
        assignments = CourseDiaryDB.getDBInstance(context).courseDAO().getAllAssignmentsOfCourse(this);
        gradingSystem.integrateWithDB(context);
    }

    @Override
    public int getSaveMessage() {
        return R.string.course_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.courseID = CourseDiaryDB.getDBInstance(activity).courseDAO().addCourse(this);
        semester.getCourses().add(this);
        schedule(activity);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.course_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        semester.getCourses().remove(this);
        ((MainScreen)activity).getVisibleFragment().appContent = semester;
        CourseDiaryDB.getDBInstance(activity).courseDAO().deleteCourse(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).courseDAO().update(this);
    }

    @Override
    public void showInfo(final AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.INFO_MODE));
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
        boolean isChanged = creationDialog.selectSemesterOnSpinner(semester);
        creationDialog.selectedSemester = (Semester)creationDialog.semesterSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof CourseCreationDialog)
            return;
        if (isChanged || creationDialog.courseSelectionSpinner.getAdapter() == null) {
            ListAdapter<Course> adapter2 = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedSemester.getCourses(), R.layout.list_item);
            creationDialog.courseSelectionSpinner.setAdapter(adapter2);
        }
        creationDialog.selectCourseOnSpinner(this);
        if (creationDialog instanceof NoteCreationDialog || creationDialog instanceof PhotoCreationDialog)
            return;
        creationDialog.selectedCourse = (Course) creationDialog.courseSelectionSpinner.getSelectedItem();
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}
