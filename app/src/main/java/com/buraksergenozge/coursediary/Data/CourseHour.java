package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.CourseFragment;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseHourCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.Tools.StringManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Course.class, parentColumns = "courseID", childColumns = "course", onDelete = CASCADE),
        indices = {@Index("course")})
public class CourseHour extends AppContent implements Comparable<CourseHour>{
    @PrimaryKey(autoGenerate = true)
    private long courseHourID;
    @ColumnInfo
    private Course course;
    @ColumnInfo
    private Calendar startDate;
    @ColumnInfo
    private Calendar endDate;
    @ColumnInfo
    private int attendance;
    @ColumnInfo
    private int cancelled;
    @Ignore
    private List<Note> notes = new ArrayList<>();
    @Ignore
    private List<Photo> photos = new ArrayList<>();
    @Ignore
    private List<Audio> audios = new ArrayList<>();
    @Ignore
    private static String[] relatedFragmentTags = {CourseFragment.tag, CourseFeed.tag, CourseHourFragment.tag};

    public CourseHour(Course course, Calendar startDate, Calendar endDate) {
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendance = 0;
        this.cancelled = 0;
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

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel(Context context, boolean cancel) {
        if (cancel)
            cancelled = 1;
        else
            cancelled = 0;
        CourseDiaryDB.getDBInstance(context).courseHourDAO().update(this);
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public void setAudios(List<Audio> audios) {
        this.audios = audios;
    }

    public static DialogFragment getCreationDialog(boolean isEditMode) {
        CreationDialog creationDialog = new CourseHourCreationDialog();
        creationDialog.isEditMode = isEditMode;
        return creationDialog;
    }

    public void integrateWithDB(Context context) {
        notes = CourseDiaryDB.getDBInstance(context).noteDAO().getAllNotesOfCourseHour(this);
        photos = new ArrayList<>();
        File contentDir = new File(getContentDirectory());
        if (contentDir.exists()) {
            for (File photo: contentDir.listFiles()) {
                if (photo.getAbsolutePath().endsWith(".jpg")){
                    photos.add(new Photo(this, photo.getAbsolutePath()));
                }
            }
        }
        else
            contentDir.mkdir();
        //TODO: Sesleri al.
    }

    @Override
    public int getSaveMessage() {
        return R.string.course_hour_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.courseHourID = CourseDiaryDB.getDBInstance(activity).courseHourDAO().addCourseHour(this);
        course.getCourseHours().add(this);
        File contentDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ("Course Diary/" + courseHourID));
        if (!contentDir.exists())
            contentDir.mkdir();
    }

    @Override
    public int getDeleteMessage() {
        return R.string.course_hour_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        course.getCourseHours().remove(this);
        ((MainScreen)activity).getVisibleFragment().appContent = course;
        CourseDiaryDB.getDBInstance(activity).courseHourDAO().deleteCourseHour(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).courseHourDAO().update(this);
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
            ListAdapter<Course> adapter = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedSemester.getCourses(), R.layout.list_item);
            creationDialog.courseSelectionSpinner.setAdapter(adapter);
        }
        isChanged = creationDialog.selectCourseOnSpinner(course);
        creationDialog.selectedCourse = (Course) creationDialog.courseSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof AssignmentCreationDialog || creationDialog instanceof CourseHourCreationDialog)
            return;
        if (isChanged || creationDialog.courseHourSelectionSpinner.getAdapter() == null) {
            ListAdapter<CourseHour> adapter2 = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedCourse.getCourseHours(), R.layout.list_item);
            creationDialog.courseHourSelectionSpinner.setAdapter(adapter2);
        }
        creationDialog.selectCourseHourOnSpinner(this);
        creationDialog.selectedCourseHour = (CourseHour) creationDialog.courseHourSelectionSpinner.getSelectedItem();
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    public String getContentDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath().concat("/CourseDiary/" + courseHourID + "/");
    }

    @Override
    @NonNull
    public String toString() {
        String repr;
        if (startDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH))
            repr = StringManager.getDateString(startDate, "d MMM EEE") + " (" + StringManager.getDateString(startDate, "HH:mm") + " - " + StringManager.getDateString(endDate, "HH:mm") + ")";
        else
            repr = StringManager.getDateString(startDate, "d MMM EEE") + " " + StringManager.getDateString(startDate, "HH:mm") + " - " + StringManager.getDateString(endDate, "d MMM EEE") + " " + StringManager.getDateString(endDate, "HH:mm");
        return repr;
    }

    @Override
    public int compareTo(CourseHour courseHour) {
        return (int)(startDate.getTimeInMillis() - courseHour.startDate.getTimeInMillis());
    }
}