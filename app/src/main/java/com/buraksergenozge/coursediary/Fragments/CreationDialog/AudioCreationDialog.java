package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.io.File;
import java.util.Objects;

public class AudioCreationDialog extends CreationDialog {
    private Button createButton;
    private TextView name;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_audiocreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_photo));
        name = getView().findViewById(R.id.audioCreation_TV);
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
    protected void initializeEditMode() {
        appContent = MainScreen.activeAppContent;
        createButton.setText(getString(R.string.save));
    }

    @Override
    protected void initializeInfoMode() {
        appContent = MainScreen.activeAppContent;
        createButton.setVisibility(View.GONE);
        semesterSelectionSpinner.setEnabled(false);
        courseSelectionSpinner.setEnabled(false);
        courseHourSelectionSpinner.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.audioCreateButton:
                if (checkInputValidity()) {
                    if (mode == EDIT_MODE) {
                        /*if (((Audio)appContent).getCourseHour().getCourseHourID() != selectedCourseHour.getCourseHourID()) {
                            ((CourseHour)((MainScreen) Objects.requireNonNull(getActivity())).getVisibleFragment().parentFragment.appContent).getAudios().remove(appContent);
                            selectedCourseHour.getAudios().add((Audio) appContent);
                            ((Audio)appContent).setCourseHour(selectedCourseHour);
                        }
                        appContent.updateOperation((MainScreen) getActivity());*/
                    }
                    else {
                        File newLocation = new File(selectedCourseHour.getContentDirectory(), Audio.currentAudioName);
                        ((Audio)appContent).getFile().renameTo(newLocation); // Move file to related course hour's directory
                        ((Audio)appContent).setFilePath(newLocation.getAbsolutePath()); // Update file's path
                        ((Audio)appContent).setCourseHour(selectedCourseHour); // Update photo's course hour
                        appContent.addOperation((MainScreen) getActivity());
                    }
                    this.dismiss();
                    if (appContent != null) {
                        mListener.updateViewsOfAppContent(appContent);
                        MainScreen.showSnackbarMessage(Objects.requireNonNull(getActivity()).getWindow().getDecorView(), getString(appContent.getSaveMessage()));
                    }
                }
                break;
            default:
                break;
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