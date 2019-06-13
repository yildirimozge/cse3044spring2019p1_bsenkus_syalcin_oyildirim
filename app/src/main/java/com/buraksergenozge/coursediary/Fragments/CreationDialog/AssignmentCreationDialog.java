package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.RegexChecker;

import java.util.Calendar;
import java.util.Objects;

public class AssignmentCreationDialog extends CreationDialog {
    private String title = "";
    private Calendar deadline;
    private EditText assignmentTitle_ET, deadlineEditText, assignmentEndTime_ET;
    private int year, month, day, hour, minute;
    private Button createButton;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_assignmentcreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_assignment));
        deadline = Calendar.getInstance();
        assignmentTitle_ET = Objects.requireNonNull(getView()).findViewById(R.id.assignmentTitle_ET);
        createButton = getView().findViewById(R.id.assignmentCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.assignmentCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        courseSelectionSpinner = getView().findViewById(R.id.courseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        assignmentEndTime_ET = getView().findViewById(R.id.assignment_end_time_ET);
        assignmentEndTime_ET.setOnClickListener(this);
        deadlineEditText = getView().findViewById(R.id.assignment_creation_deadline_ET);
        deadlineEditText.setText(getResources().getString(R.string.date_format, day, (month + 1), year));
        assignmentEndTime_ET.setText(getResources().getString(R.string.clock_format, hour, minute));
        deadlineEditText.setOnClickListener(this);
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
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(((Assignment)appContent).getTitle());
        assignmentTitle_ET.setText(((Assignment) appContent).getTitle());
        deadlineEditText.setText(getResources().getString(R.string.date_format, ((Assignment) appContent).getDeadline().get(Calendar.DAY_OF_MONTH), (((Assignment) appContent).getDeadline().get(Calendar.MONTH) + 1), ((Assignment) appContent).getDeadline().get(Calendar.YEAR)));
        assignmentEndTime_ET.setText(getResources().getString(R.string.clock_format, ((Assignment) appContent).getDeadline().get(Calendar.HOUR_OF_DAY), ((Assignment) appContent).getDeadline().get(Calendar.MINUTE)));
        createButton.setText(getString(R.string.save));
    }

    @Override
    protected void initializeInfoMode() {
        appContent = MainScreen.activeAppContent;
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(((Assignment)appContent).getTitle());
        assignmentTitle_ET.setText(((Assignment) appContent).getTitle());
        assignmentTitle_ET.setEnabled(false);
        deadlineEditText.setText(getResources().getString(R.string.date_format, ((Assignment) appContent).getDeadline().get(Calendar.DAY_OF_MONTH), (((Assignment) appContent).getDeadline().get(Calendar.MONTH) + 1), ((Assignment) appContent).getDeadline().get(Calendar.YEAR)));
        deadlineEditText.setEnabled(false);
        assignmentEndTime_ET.setText(getResources().getString(R.string.clock_format, ((Assignment) appContent).getDeadline().get(Calendar.HOUR_OF_DAY), ((Assignment) appContent).getDeadline().get(Calendar.MINUTE)));
        assignmentEndTime_ET.setEnabled(false);
        createButton.setVisibility(View.GONE);
        semesterSelectionSpinner.setEnabled(false);
        courseSelectionSpinner.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.assignmentCreateButton:
                if (checkInputValidity()) {
                    if (mode == EDIT_MODE) {
                        ((Assignment)appContent).setTitle(title);
                        ((Assignment) appContent).setDeadline(deadline);
                        if (((Assignment)appContent).getCourse().getCourseID() != selectedCourse.getCourseID()) {
                            ((Course)((MainScreen) Objects.requireNonNull(getActivity())).getVisibleFragment().parentFragment.appContent).getAssignments().remove(appContent);
                            ((Assignment)appContent).setCourse(selectedCourse);
                            selectedCourse.getAssignments().add((Assignment) appContent);
                        }
                        appContent.updateOperation((MainScreen) getActivity());
                    }
                    else {
                        appContent = new Assignment(title, selectedCourse, deadline);
                        appContent.addOperation((MainScreen) getActivity());
                    }
                    this.dismiss();
                    mListener.updateViewsOfAppContent(appContent);
                    MainScreen.showSnackbarMessage(Objects.requireNonNull(getActivity()).getWindow().getDecorView(), getString(appContent.getSaveMessage()));
                }
                break;
            case R.id.assignment_creation_deadline_ET:
                new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        deadlineEditText.setText(getResources().getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), year));
                    }
                }, year, month, day).show();
                break;
            case R.id.assignment_end_time_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        assignmentEndTime_ET.setText(getResources().getString(R.string.clock_format, hourOfDay, minute));
                    }
                }, hour, minute, true).show();
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
        String timeString = assignmentEndTime_ET.getText().toString().trim();
        if (!RegexChecker.check(timeString, RegexChecker.clockPattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_time), Toast.LENGTH_SHORT).show();
            return false;
        }
        tokens = timeString.split(":");
        hour = Integer.parseInt(tokens[0]);
        minute = Integer.parseInt(tokens[1]);
        deadline.set(year, month - 1, day, hour, minute);
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
}