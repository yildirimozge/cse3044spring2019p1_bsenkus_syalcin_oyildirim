package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.Calendar;
import java.util.List;

public class AssignmentCreationDialog extends CreationDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button createButton;
    private EditText nameEditText, deadlineEditText;
    private Spinner semesterSelectionSpinner, courseSelectionSpinner;
    private ImageView closeIcon;
    private int year, month, day;
    private List<Course> courses;
    private Semester selectedSemester = null;
    private Course selectedCourse = null;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.dialogfragment_assignmentcreation;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        for (Semester semester: User.getSemesters()) { // If there is at least 1 course
            if (!semester.getCourses().isEmpty())
                return;
        }
        Toast.makeText(getContext(), "You have not any course to add an assignment!", Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        nameEditText = getView().findViewById(R.id.semesterNameEditText);
        closeIcon = getView().findViewById(R.id.assignmentCreationCloseIcon);
        closeIcon.setOnClickListener(this);
        createButton = getView().findViewById(R.id.assignmentCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.assignmentCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters());
        semesterSelectionSpinner.setAdapter(adapter);
        courseSelectionSpinner = getView().findViewById(R.id.courseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        deadlineEditText = getView().findViewById(R.id.deadlineView);
        deadlineEditText.setText(day + "-" + (month + 1) + "-" + year);
        deadlineEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.assignmentCreationCloseIcon:
                this.dismiss();
                break;
            case R.id.assignmentCreateButton:
                if(selectedCourse != null) {
                    Calendar deadline = Calendar.getInstance();
                    deadline.set(year, month, day);
                    String title = nameEditText.getText().toString();
                    if (!checkInputValidity(title, deadline)) {
                        Toast.makeText(getContext(), "Invalid inputs!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Assignment newAssignment = new Assignment(title, selectedCourse, deadline);
                    selectedCourse.addAssignment(getContext(), newAssignment);
                    this.dismiss();
                    if(mListener != null)
                        mListener.onAssignmentOperation(getString(R.string.assignment_created));
                }
                else {
                    Toast.makeText(getContext(), "Please select a course.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.deadlineTextView:
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        deadlineEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day).show();
                break;
            default:
                break;
        }
    }

    private boolean checkInputValidity(String title, Calendar deadline) {
        if (title.length() == 0)
            return false;
        if (!deadline.after(Calendar.getInstance()))
            return false;
        if (selectedCourse == null && selectedSemester == null)
            return false;
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == semesterSelectionSpinner) {
            selectedSemester = (Semester) adapterView.getSelectedItem();
            courses = CourseDiaryDB.getDBInstance(getContext()).semesterDAO().getAllCoursesOfSemester(selectedSemester);
            ListAdapter<Course> adapter = new ListAdapter<>(getActivity(), courses);
            courseSelectionSpinner.setAdapter(adapter);
        }
        else if (adapterView == courseSelectionSpinner)
            selectedCourse = (Course)adapterView.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedCourse = null;
    }

    public interface OnFragmentInteractionListener {
        void onAssignmentOperation(String message);
    }
}