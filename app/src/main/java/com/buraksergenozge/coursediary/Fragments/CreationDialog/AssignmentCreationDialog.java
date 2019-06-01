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
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.RegexChecker;

import java.util.Calendar;

public class AssignmentCreationDialog extends CreationDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String title = "";
    private Calendar deadline;
    private EditText assignmentTitle_ET, deadlineEditText;
    private Spinner semesterSelectionSpinner, courseSelectionSpinner;
    private int year, month, day;
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
        deadline = Calendar.getInstance();
        assignmentTitle_ET = getView().findViewById(R.id.assignmentTitle_ET);
        ImageView closeIcon = getView().findViewById(R.id.assignmentCreationCloseIcon);
        closeIcon.setOnClickListener(this);
        Button createButton = getView().findViewById(R.id.assignmentCreateButton);
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
        if (BaseFragment.contextObject instanceof Course) {
            Semester semester = (Semester) semesterSelectionSpinner.getSelectedItem();
            if (semester.getSemesterID() != ((Course)BaseFragment.contextObject).getSemester().getSemesterID()) {
                int count = semesterSelectionSpinner.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    semester = (Semester) semesterSelectionSpinner.getItemAtPosition(i);
                    if (((Course)BaseFragment.contextObject).getSemester().getSemesterID() == semester.getSemesterID()) {
                        semesterSelectionSpinner.setSelection(i);
                        break;
                    }
                }
            }

            ListAdapter<Course> adapter2 = new ListAdapter<>(getActivity(), selectedSemester.getCourses());
            courseSelectionSpinner.setAdapter(adapter2);
            int count = courseSelectionSpinner.getAdapter().getCount();
            for (int i = 0; i < count; i++) {
                Course course = (Course) courseSelectionSpinner.getItemAtPosition(i);
                if (((Course)BaseFragment.contextObject).getCourseID() == course.getCourseID()) {
                    courseSelectionSpinner.setSelection(i);
                    break;
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.assignmentCreationCloseIcon:
                this.dismiss();
                break;
            case R.id.assignmentCreateButton:
                if(selectedCourse != null) {
                    if (!checkInputValidity()) {
                        Toast.makeText(getContext(), "Invalid inputs!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Assignment newAssignment = new Assignment(title, selectedCourse, deadline);
                    selectedCourse.addAssignment(getContext(), newAssignment);
                    this.dismiss();
                    if(mListener != null)
                        mListener.onAppContentOperation("courseFragment", getString(R.string.assignment_created));
                }
                else
                    Toast.makeText(getContext(), "Please select a course.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deadlineView:
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

    private boolean checkInputValidity() {
        title = assignmentTitle_ET.getText().toString().trim();
        if (title.length() == 0) {
            Toast.makeText(getContext(), getString(R.string.invalid_assignment_title), Toast.LENGTH_SHORT).show();
            return false;
        }
        String deadlineString = deadlineEditText.getText().toString().trim();
        if (!RegexChecker.check(deadlineString, RegexChecker.datePattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_deadline), Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] tokens = deadlineString.split("-");
        day = Integer.parseInt(tokens[0]);
        month = Integer.parseInt(tokens[1]);
        year = Integer.parseInt(tokens[2]);
        deadline.set(year, month - 1, day, 23, 59);
        if (!deadline.after(Calendar.getInstance())) {
            Toast.makeText(getContext(), getString(R.string.deadline_past), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == semesterSelectionSpinner) {
            if (selectedSemester != adapterView.getSelectedItem()) {
                selectedSemester = (Semester) adapterView.getSelectedItem();
                ListAdapter<Course> adapter = new ListAdapter<>(getActivity(), selectedSemester.getCourses());
                courseSelectionSpinner.setAdapter(adapter);
            }
        }
        else if (adapterView == courseSelectionSpinner)
            selectedCourse = (Course)adapterView.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (adapterView == semesterSelectionSpinner)
            selectedSemester = null;
        else if (adapterView == courseSelectionSpinner)
            selectedCourse = null;
    }
}
