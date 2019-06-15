package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.io.File;
import java.util.Objects;

public class AudioCreationDialog extends CreationDialog {

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_audiocreation;
    }

    @Override
    protected void initializeViews() {
        super.initializeViews();
        toolbarTitle_TV.setText(getString(R.string.new_photo));
        TextView name = Objects.requireNonNull(getView()).findViewById(R.id.audioCreation_TV);
        if (mode == INFO_MODE)
            name.setText(((Audio)MainScreen.contextMenuAppContent).name);
        else
            name.setText(((Audio)appContent).name);
        createButton = getView().findViewById(R.id.audioCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.audioCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        courseSelectionSpinner = getView().findViewById(R.id.audioCreationCourseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        courseHourSelectionSpinner = getView().findViewById(R.id.audioCreationCourseHourSelectionSpinner);
        courseHourSelectionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void prepareSpinners() {
        if(MainScreen.activeAppContent != null)
            MainScreen.activeAppContent.fillSpinners(this);
        else {
            ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters(), R.layout.list_item);
            semesterSelectionSpinner.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.audioCreateButton) {
            if (checkInputValidity()) {
                if (mode == CREATE_MODE) {
                    File newLocation = new File(selectedCourseHour.getContentDirectory(), Audio.currentAudioName);
                    ((Audio)appContent).getFile().renameTo(newLocation); // Move file to related course hour's directory
                    ((Audio)appContent).setFilePath(newLocation.getAbsolutePath()); // Update file's path
                    ((Audio)appContent).setCourseHour(selectedCourseHour); // Update photo's course hour
                    appContent.create((MainScreen) getActivity());
                }
                this.dismiss();
            }
        }
    }

    private boolean checkInputValidity() {
        if (selectedCourseHour == null) {
            Toast.makeText(getContext(), getString(R.string.missing_course_hour), Toast.LENGTH_SHORT).show();
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