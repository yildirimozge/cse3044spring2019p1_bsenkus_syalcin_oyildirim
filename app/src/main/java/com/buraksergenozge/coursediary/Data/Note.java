package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseHourCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.NoteCreationDialog;
import com.buraksergenozge.coursediary.Fragments.NoteFragment;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;

import java.util.Calendar;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = CourseHour.class, parentColumns = "courseHourID", childColumns = "courseHour", onDelete = CASCADE),
        indices = {@Index("courseHour")})
public class Note extends AppContent {
    @PrimaryKey(autoGenerate = true)
    private long noteID;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private CourseHour courseHour;
    @ColumnInfo
    private String text;
    @ColumnInfo
    private Calendar createDate;
    @Ignore
    private static final String[] relatedFragmentTags = {CourseHourFragment.tag, NoteFragment.tag};

    public Note(CourseHour courseHour, String title, String text) {
        this.courseHour = courseHour;
        this.title = title;
        this.text = text;
        createDate = (Calendar)Calendar.getInstance().clone();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public static CreationDialog getCreationDialog(int mode) {
        CreationDialog creationDialog = new NoteCreationDialog();
        creationDialog.mode = mode;
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.EDIT_MODE));
    }

    @Override
    public int getSaveMessage() {
        return R.string.note_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.noteID = CourseDiaryDB.getDBInstance(activity).noteDAO().addNote(this);
        courseHour.getNotes().add(this);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.note_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        ((CourseHour)((MainScreen)activity).getVisibleFragment().appContent).getNotes().remove(this);
        CourseDiaryDB.getDBInstance(activity).noteDAO().deleteNote(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).noteDAO().update(this);
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
        boolean isChanged = creationDialog.selectSemesterOnSpinner(courseHour.getCourse().getSemester());
        creationDialog.selectedSemester = (Semester)creationDialog.semesterSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof CourseCreationDialog)
            return;
        if (isChanged || creationDialog.courseSelectionSpinner.getAdapter() == null) {
            ListAdapter<Course> adapter = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedSemester.getCourses(), R.layout.list_item);
            creationDialog.courseSelectionSpinner.setAdapter(adapter);
        }
        isChanged = creationDialog.selectCourseOnSpinner(courseHour.getCourse());
        creationDialog.selectedCourse = (Course) creationDialog.courseSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof AssignmentCreationDialog || creationDialog instanceof CourseHourCreationDialog)
            return;
        if (isChanged || creationDialog.courseHourSelectionSpinner.getAdapter() == null) {
            ListAdapter<CourseHour> adapter2 = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedCourse.getCourseHours(), R.layout.list_item);
            creationDialog.courseHourSelectionSpinner.setAdapter(adapter2);
        }
        creationDialog.selectCourseHourOnSpinner(courseHour);
        creationDialog.selectedCourseHour = (CourseHour) creationDialog.courseHourSelectionSpinner.getSelectedItem();
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    @Override
    public void showInfo(final AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.INFO_MODE));
    }

    @Override
    public String toString() {
        return title;
    }
}