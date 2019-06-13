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
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.RegexChecker;

import java.util.Calendar;
import java.util.Objects;

public class CourseHourCreationDialog extends CreationDialog {
    private Calendar startDate, endDate;
    private int sYear, sMonth, sDay, sHour, sMinute, eYear, eMonth, eDay, eHour, eMinute;
    private EditText startDay_ET, endDay_ET, startTime_ET, endTime_ET;
    private Button createButton;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_coursehourcreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_course_hour));
        createButton = getView().findViewById(R.id.courseHourCreateButton);
        createButton.setOnClickListener(this);
        semesterSelectionSpinner = getView().findViewById(R.id.courseHourCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        courseSelectionSpinner = getView().findViewById(R.id.courseHourCreationCourseSelectionSpinner);
        courseSelectionSpinner.setOnItemSelectedListener(this);
        startDay_ET = getView().findViewById(R.id.start_date_ET);
        endDay_ET = getView().findViewById(R.id.end_date_ET);
        startTime_ET = getView().findViewById(R.id.start_time_ET);
        endTime_ET = getView().findViewById(R.id.end_time_ET);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
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
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(appContent.toString());
        startDay_ET.setText(getResources().getString(R.string.date_format, ((CourseHour) appContent).getStartDate().get(Calendar.DAY_OF_MONTH), ((CourseHour) appContent).getStartDate().get(Calendar.MONTH) + 1, ((CourseHour) appContent).getStartDate().get(Calendar.YEAR)));
        endDay_ET.setText(getResources().getString(R.string.date_format, ((CourseHour) appContent).getEndDate().get(Calendar.DAY_OF_MONTH), ((CourseHour) appContent).getEndDate().get(Calendar.MONTH) + 1, ((CourseHour) appContent).getEndDate().get(Calendar.YEAR)));
        startTime_ET.setText(getResources().getString(R.string.clock_format, ((CourseHour) appContent).getStartDate().get(Calendar.HOUR_OF_DAY), ((CourseHour) appContent).getStartDate().get(Calendar.MINUTE)));
        endTime_ET.setText(getResources().getString(R.string.clock_format, ((CourseHour) appContent).getEndDate().get(Calendar.HOUR_OF_DAY), ((CourseHour) appContent).getEndDate().get(Calendar.MINUTE)));
        createButton.setText(getString(R.string.save));
    }

    @Override
    protected void initializeInfoMode() {
        appContent = MainScreen.activeAppContent;
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(appContent.toString());
        startDay_ET.setText(getResources().getString(R.string.date_format, ((CourseHour) appContent).getStartDate().get(Calendar.DAY_OF_MONTH), ((CourseHour) appContent).getStartDate().get(Calendar.MONTH) + 1, ((CourseHour) appContent).getStartDate().get(Calendar.YEAR)));
        startDay_ET.setEnabled(false);
        endDay_ET.setText(getResources().getString(R.string.date_format, ((CourseHour) appContent).getEndDate().get(Calendar.DAY_OF_MONTH), ((CourseHour) appContent).getEndDate().get(Calendar.MONTH) + 1, ((CourseHour) appContent).getEndDate().get(Calendar.YEAR)));
        endDay_ET.setEnabled(false);
        startTime_ET.setText(getResources().getString(R.string.clock_format, ((CourseHour) appContent).getStartDate().get(Calendar.HOUR_OF_DAY), ((CourseHour) appContent).getStartDate().get(Calendar.MINUTE)));
        startTime_ET.setEnabled(false);
        endTime_ET.setText(getResources().getString(R.string.clock_format, ((CourseHour) appContent).getEndDate().get(Calendar.HOUR_OF_DAY), ((CourseHour) appContent).getEndDate().get(Calendar.MINUTE)));
        endTime_ET.setEnabled(false);
        createButton.setVisibility(View.GONE);
        semesterSelectionSpinner.setEnabled(false);
        courseSelectionSpinner.setEnabled(false);
    }

    private boolean checkInputValidity() {
        String dateString = startDay_ET.getText().toString().trim();
        if (!RegexChecker.check(dateString, RegexChecker.datePattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_start_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        String timeString = startTime_ET.getText().toString().trim();
        if (!RegexChecker.check(timeString, RegexChecker.clockPattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_start_time), Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] tokens = dateString.split("-");
        sDay = Integer.parseInt(tokens[0]);
        sMonth = Integer.parseInt(tokens[1]);
        sYear = Integer.parseInt(tokens[2]);
        String[] timeTokens = timeString.split(":");
        sHour = Integer.parseInt(timeTokens[0]);
        sMinute = Integer.parseInt(timeTokens[1]);
        startDate.set(sYear, sMonth - 1, sDay, sHour, sMinute);
        dateString = endDay_ET.getText().toString().trim();
        if (!RegexChecker.check(dateString, RegexChecker.datePattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        timeString = endTime_ET.getText().toString().trim();
        if (!RegexChecker.check(timeString, RegexChecker.clockPattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_time), Toast.LENGTH_SHORT).show();
            return false;
        }
        tokens = dateString.split("-");
        eDay = Integer.parseInt(tokens[0]);
        eMonth = Integer.parseInt(tokens[1]);
        eYear = Integer.parseInt(tokens[2]);
        timeTokens = timeString.split(":");
        eHour = Integer.parseInt(timeTokens[0]);
        eMinute = Integer.parseInt(timeTokens[1]);
        endDate.set(eYear, eMonth - 1, eDay, eHour, eMinute);
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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.courseHourCreateButton:
                if (checkInputValidity()) {
                    if (mode == EDIT_MODE) {
                        ((CourseHour)appContent).setEndDate(endDate);
                        ((CourseHour)appContent).setStartDate(startDate);
                        if (((CourseHour)appContent).getCourse().getCourseID() != selectedCourse.getCourseID()) {
                            ((Course)((MainScreen) Objects.requireNonNull(getActivity())).getVisibleFragment().parentFragment.appContent).getCourseHours().remove(appContent);
                            ((CourseHour)appContent).setCourse(selectedCourse);
                            selectedCourse.getCourseHours().add((CourseHour) appContent);
                        }
                        appContent.updateOperation((MainScreen) getActivity());
                    }
                    else {
                        appContent = new CourseHour(selectedCourse, startDate, endDate);
                        appContent.addOperation((MainScreen) getActivity());
                    }
                    this.dismiss();
                    mListener.updateViewsOfAppContent(appContent);
                    MainScreen.showSnackbarMessage(Objects.requireNonNull(getActivity()).getWindow().getDecorView(), getString(appContent.getSaveMessage()));
                }
                break;
            case R.id.start_date_ET:
                new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDay_ET.setText(getResources().getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), dayOfMonth));
                    }
                }, sYear, sMonth, sDay).show();
                break;
            case R.id.end_date_ET:
                new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDay_ET.setText(getResources().getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), dayOfMonth));
                    }
                }, eYear, eMonth, eDay).show();
                break;
            case R.id.start_time_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime_ET.setText(getResources().getString(R.string.clock_format, hourOfDay, minute));
                        eHour = (hourOfDay + 1) % 24;
                        eMinute = minute;
                        endTime_ET.setText(getResources().getString(R.string.clock_format, eHour, eMinute));
                    }
                }, sHour, sMinute, true).show();
                break;
            case R.id.end_time_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime_ET.setText(getResources().getString(R.string.clock_format, hourOfDay, minute));
                    }
                }, eHour, eMinute, true).show();
                break;
            default:
                break;
        }
    }
}