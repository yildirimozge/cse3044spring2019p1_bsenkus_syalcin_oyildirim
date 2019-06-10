package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.io.File;
import java.util.Objects;

public class PhotoCreationDialog extends CreationDialog {

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_photocreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_photo));
        Button createButton = getView().findViewById(R.id.photoCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.photoCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        courseSelectionSpinner = getView().findViewById(R.id.photoCreationCourseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        courseHourSelectionSpinner = getView().findViewById(R.id.photoCreationCourseHourSelectionSpinner);
        courseHourSelectionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void prepareSpinners() {
        if(MainScreen.activeAppContent != null)
            MainScreen.activeAppContent.fillSpinners(this);
        else {
            ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters(), R.layout.list_item);
            semesterSelectionSpinner.setAdapter(adapter);
        }    }

    @Override
    protected void initializeEditMode() {
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.photoCreateButton:
                AppContent appContent = null;
                if (checkInputValidity()) {
                    if (isEditMode)
                        Toast.makeText(getActivity(), "EDIT MODE", Toast.LENGTH_SHORT).show();
                    else {
                        File newLocation = new File(selectedCourseHour.getContentDirectory(), Photo.currentPhotoName);
                        File path = new File(Photo.getCurrentPhotoPath());
                        path.renameTo(newLocation);
                        appContent = new Photo(selectedCourseHour, newLocation.getAbsolutePath());
                        appContent.addOperation((MainScreen) getActivity());
                    }
                    this.dismiss();
                    if (appContent != null) {
                        mListener.updateViewsOfAppContent(appContent);
                        MainScreen.showSnackbarMessage(getView(), getString(appContent.getSaveMessage()));
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean checkInputValidity() {
        if (selectedCourseHour == null) {
            Toast.makeText(getContext(), "SELECT COURSE HOUR", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedCourse == null) {
            Toast.makeText(getContext(), getString(R.string.missing_course_selection), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedSemester == null) {
            Toast.makeText(getContext(), getString(R.string.missing_semester_selection), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}