package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.ArrayList;
import java.util.Objects;

public abstract class CreationDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    OnFragmentInteractionListener mListener;
    int closeIconID;
    public Spinner semesterSelectionSpinner, courseSelectionSpinner, courseHourSelectionSpinner;
    public Semester selectedSemester = null;
    public Course selectedCourse = null;
    public  CourseHour selectedCourseHour = null;
    public boolean isEditMode = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
        initializeViews();
        prepareSpinners();
        if(isEditMode)
            initializeEditMode();
    }

    protected abstract int getLayoutID();

    protected abstract void prepareSpinners();

    protected abstract void initializeViews();

    protected abstract void initializeEditMode();

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

    @Override
    public void onClick(View view) {
        if (view.getId() == closeIconID)
            this.dismiss();
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

    public interface OnFragmentInteractionListener {
        void updateViewsOfAppContent(AppContent appContent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}