package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.AssignmentFragment;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.CourseFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.NoteCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.PhotoCreationDialog;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;

import java.util.Calendar;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "courseID", childColumns = "course", onDelete = CASCADE),
        indices = {@Index("course")})
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
    private static String[] relatedFragmentTags = {AssignmentFragment.tag, CourseFragment.tag, CourseFeed.tag};

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

    public static DialogFragment getCreationDialog(boolean isEditMode) {
        CreationDialog creationDialog = new AssignmentCreationDialog();
        creationDialog.isEditMode = isEditMode;
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(true));
    }

    public long getRemainingTimeInMillis() {
        Calendar now = Calendar.getInstance();
        if (deadline.getTimeInMillis() - now.getTimeInMillis() < 0)
            return 0;
        return deadline.getTimeInMillis() - now.getTimeInMillis();
    }

    @Override
    public int getSaveMessage() {
        return R.string.assignment_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.assignmentID = CourseDiaryDB.getDBInstance(activity).assignmentDAO().addAssignment(this);
        course.getAssignments().add(this);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.assignment_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        course.getAssignments().remove(this);
        ((MainScreen)activity).getVisibleFragment().appContent = course;
        CourseDiaryDB.getDBInstance(activity).assignmentDAO().deleteAssignment(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).assignmentDAO().update(this);
    }

    @Override
    public void showInfo(final AppCompatActivity activity) {
        Toast.makeText(activity, toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
        boolean isChanged = creationDialog.selectSemesterOnSpinner(course.getSemester());
        creationDialog.selectedSemester = (Semester)creationDialog.semesterSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof CourseCreationDialog)
            return;
        if (isChanged || creationDialog.courseSelectionSpinner.getAdapter() == null) {
            ListAdapter<Course> adapter2 = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedSemester.getCourses(), R.layout.list_item);
            creationDialog.courseSelectionSpinner.setAdapter(adapter2);
        }
        creationDialog.selectCourseOnSpinner(course);
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
        return title;
    }
}