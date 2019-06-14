package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class NoteCreationDialog extends CreationDialog {
    private String title, text;
    private EditText noteTitle_ET, noteText_ET;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_notecreation;
    }

    @Override
    protected void initializeViews() {
        super.initializeViews();
        toolbarTitle_TV.setText(getString(R.string.new_note));
        noteTitle_ET = Objects.requireNonNull(getView()).findViewById(R.id.noteNameEditText);
        noteText_ET = getView().findViewById(R.id.note_ET);
        createButton = getView().findViewById(R.id.noteCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.noteCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        courseSelectionSpinner = getView().findViewById(R.id.noteCreationCourseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        courseHourSelectionSpinner = getView().findViewById(R.id.noteCreationCourseHourSelectionSpinner);
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
        super.initializeEditMode();
        noteTitle_ET.setText(((Note) appContent).getTitle());
        noteText_ET.setText(((Note) appContent).getText());
    }

    @Override
    protected void initializeInfoMode() {
        super.initializeInfoMode();
        noteTitle_ET.setText(((Note) appContent).getTitle());
        noteTitle_ET.setEnabled(false);
        noteText_ET.setText(((Note) appContent).getText());
        noteText_ET.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.noteCreateButton) {
            if (checkInputValidity()) {
                if (mode == EDIT_MODE) {
                    ((Note)appContent).setTitle(title);
                    ((Note)appContent).setText(text);
                    if (((Note)appContent).getCourseHour().getCourseHourID() != selectedCourseHour.getCourseHourID()) {
                        ((CourseHour)((MainScreen) Objects.requireNonNull(getActivity())).getVisibleFragment().parentFragment.appContent).getNotes().remove(appContent);
                        ((Note)appContent).setCourseHour(selectedCourseHour);
                        selectedCourseHour.getNotes().add((Note) appContent);
                    }
                    appContent.updateOperation((MainScreen) getActivity());
                }
                else {
                    appContent = new Note(selectedCourseHour, title, text);
                    appContent.create((MainScreen) getActivity());
                }
                this.dismiss();
            }
        }
    }

    private boolean checkInputValidity() {
        title = noteTitle_ET.getText().toString().trim();
        if (title.length() == 0) {
            Toast.makeText(getContext(), getString(R.string.invalid_note_title), Toast.LENGTH_SHORT).show();
            return false;
        }
        text = noteText_ET.getText().toString().trim();
        if (text.length() == 0) {
            Toast.makeText(getContext(), "INVALID TEXT", Toast.LENGTH_SHORT).show();
            return false;
        }
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