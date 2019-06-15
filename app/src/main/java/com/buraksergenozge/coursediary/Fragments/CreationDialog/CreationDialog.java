package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.ArrayList;
import java.util.Objects;

public abstract class CreationDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public Spinner semesterSelectionSpinner, courseSelectionSpinner, courseHourSelectionSpinner;
    public Semester selectedSemester = null;
    public Course selectedCourse = null;
    public  CourseHour selectedCourseHour = null;
    Button createButton;
    TextView toolbarTitle_TV;
    public int mode = 0; // 0 for creation, 1 for edit, 2 for info
    public static final int CREATE_MODE = 0;
    public static final int EDIT_MODE = 1;
    public static final int INFO_MODE = 2;
    AppContent appContent = null;

    public void setAppContent(AppContent appContent) {
        this.appContent = appContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
        if (!checkCreationStatus()) {
            dismiss();
            return;
        }
        initializeViews();
        prepareSpinners();
        if (mode == EDIT_MODE)
            initializeEditMode();
        else if (mode == INFO_MODE)
            initializeInfoMode();
    }

    protected abstract int getLayoutID();

    protected abstract void prepareSpinners();

    void initializeViews() {
        toolbarTitle_TV = Objects.requireNonNull(getView()).findViewById(R.id.creationTitle);
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
    }

    void initializeEditMode() {
        appContent = MainScreen.activeAppContent;
        toolbarTitle_TV.setText(appContent.toString());
        createButton.setText(getString(R.string.save));
    }

    void initializeInfoMode() {
        appContent = MainScreen.contextMenuAppContent;
        toolbarTitle_TV.setText(appContent.toString());
        if (semesterSelectionSpinner != null)
            semesterSelectionSpinner.setEnabled(false);
        if (courseSelectionSpinner != null)
            courseSelectionSpinner.setEnabled(false);
        if (courseHourSelectionSpinner != null)
            courseHourSelectionSpinner.setEnabled(false);
        createButton.setVisibility(View.GONE);
    }

    public boolean selectSemesterOnSpinner(Semester semester) {
        Semester currentSelected = (Semester) semesterSelectionSpinner.getSelectedItem();
        if (currentSelected == null || currentSelected.getSemesterID() != semester.getSemesterID()) {
            if (semesterSelectionSpinner.getAdapter() == null) {
                ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters(), R.layout.list_item);
                semesterSelectionSpinner.setAdapter(adapter);
            }
            int count = semesterSelectionSpinner.getAdapter().getCount();
            for (int i = 0; i < count; i++) {
                currentSelected = (Semester) semesterSelectionSpinner.getItemAtPosition(i);
                if (semester.getSemesterID() == currentSelected.getSemesterID()) {
                    semesterSelectionSpinner.setSelection(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean selectCourseOnSpinner(Course course) {
        Course currentSelected = (Course) courseSelectionSpinner.getSelectedItem();
        if (currentSelected == null || currentSelected.getCourseID() != course.getCourseID()) {
            int count = courseSelectionSpinner.getAdapter().getCount();
            for (int i = 0; i < count; i++) {
                currentSelected = (Course) courseSelectionSpinner.getItemAtPosition(i);
                if (course.getCourseID() == currentSelected.getCourseID()) {
                    courseSelectionSpinner.setSelection(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void selectCourseHourOnSpinner(CourseHour courseHour) {
        CourseHour currentSelected = (CourseHour) courseHourSelectionSpinner.getSelectedItem();
        if (currentSelected == null || currentSelected.getCourseHourID() != courseHour.getCourseHourID()) {
            int count = courseHourSelectionSpinner.getAdapter().getCount();
            for (int i = 0; i < count; i++) {
                currentSelected = (CourseHour) courseHourSelectionSpinner.getItemAtPosition(i);
                if (courseHour.getCourseHourID() == currentSelected.getCourseHourID()) {
                    courseHourSelectionSpinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private boolean checkCreationStatus() { // Checks whether content that currently is being created can be created or not.
        if (MainScreen.activeAppContent == null) {
            if (this instanceof NoteCreationDialog) {
                if (User.getCourseHoursEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else if (this instanceof CourseCreationDialog) {
                if (User.getSemesters().isEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_semester), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else if (this instanceof AssignmentCreationDialog || this instanceof CourseHourCreationDialog) {
                if (User.getCoursesEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_course), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        else if (MainScreen.activeAppContent instanceof Semester) {
            if (this instanceof NoteCreationDialog) {
                if (((Semester)MainScreen.activeAppContent).getCourseHoursEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else if (this instanceof AssignmentCreationDialog || this instanceof CourseHourCreationDialog) {
                if (((Semester)MainScreen.activeAppContent).getCourses().isEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_course), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        else if (MainScreen.activeAppContent instanceof Course) {
            if (this instanceof NoteCreationDialog) {
                if (((Course)MainScreen.activeAppContent).getCourseHours().isEmpty()) {
                    Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.creationCloseIcon) {
            if (this instanceof PhotoCreationDialog && mode == CREATE_MODE) {
                ((Photo)appContent).getFile().delete();
            }
            this.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == semesterSelectionSpinner) {
            if (selectedSemester != adapterView.getSelectedItem() || (courseSelectionSpinner != null && (courseSelectionSpinner.getAdapter() == null || courseSelectionSpinner.getAdapter().getCount() == 0))) {
                selectedSemester = (Semester) adapterView.getSelectedItem();
                if (courseSelectionSpinner != null) { // If course spinner exists
                    ListAdapter<Course> adapter = new ListAdapter<>(getActivity(), selectedSemester.getCourses(), R.layout.list_item);
                    courseSelectionSpinner.setAdapter(adapter);
                    if (adapter.getCount() == 0 && courseHourSelectionSpinner != null)
                        courseHourSelectionSpinner.setAdapter(new ListAdapter<>(getActivity(), new ArrayList<>(), R.layout.list_item));
                }
                //TODO: Semester değiştirildiğinde, eğer ders saati spinnerı da varsa o da değiştirilmeli
            }
        }
        else if (adapterView == courseSelectionSpinner) {
            if (selectedCourse != adapterView.getSelectedItem() || (courseHourSelectionSpinner != null && (courseHourSelectionSpinner.getAdapter() == null || courseHourSelectionSpinner.getAdapter().getCount() == 0))) {
                selectedCourse = (Course) adapterView.getSelectedItem();
                if (courseHourSelectionSpinner != null) {
                    ListAdapter<CourseHour> adapter = new ListAdapter<>(getActivity(), selectedCourse.getCourseHours(), R.layout.list_item);
                    courseHourSelectionSpinner.setAdapter(adapter);
                }
            }
        }
        else if (adapterView == courseHourSelectionSpinner)
            if (selectedCourseHour != adapterView.getSelectedItem())
                selectedCourseHour = (CourseHour)adapterView.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (adapterView == semesterSelectionSpinner)
            selectedSemester = null;
        else if (adapterView == courseSelectionSpinner)
            selectedCourse = null;
        else if (adapterView == courseHourSelectionSpinner)
            selectedCourseHour = null;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }
}