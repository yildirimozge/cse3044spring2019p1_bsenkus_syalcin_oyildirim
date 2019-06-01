package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.buraksergenozge.coursediary.RegexChecker;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseCreationDialog extends CreationDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    private String courseName;
    private int credit, sHour, sMinute, eHour, eMinute;
    private float attendanceObligation;
    private EditText courseName_ET, creditET, startTime_ET, endTime_ET;
    private TextView attendanceObligationValueTV;
    private SeekBar attendanceObligationSeekBar;
    private Spinner semesterSelectionSpinner, startDaySelectionSpinner, endDaySelectionSpinner;
    private Semester selectedSemester;
    private Calendar startTime, endTime;
    private List<Calendar[]> schedule;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.dialogfragment_coursecreation;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        if (User.getSemesters().size() == 0) {
            Toast.makeText(getContext(), "You have not any semester to add a course!", Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        courseName_ET = getView().findViewById(R.id.courseName_ET);
        creditET = getView().findViewById(R.id.credit_ET);
        startTime = (Calendar) Calendar.getInstance().clone();
        endTime = (Calendar) startTime.clone();
        startTime_ET = getView().findViewById(R.id.startTime_ET);
        startTime_ET.setOnClickListener(this);
        endTime_ET = getView().findViewById(R.id.endTime_ET);
        endTime_ET.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        sHour = calendar.get(Calendar.HOUR_OF_DAY);
        sMinute = calendar.get(Calendar.MINUTE);
        startTime_ET.setText(String.format("%1$02d", sHour) + ":" + String.format("%1$02d", sMinute));
        calendar.roll(Calendar.HOUR_OF_DAY, 1);
        eHour = calendar.get(Calendar.HOUR_OF_DAY);
        eMinute = sMinute;
        endTime_ET.setText(String.format("%1$02d", eHour) + ":" + String.format("%1$02d", eMinute));
        startDaySelectionSpinner = getView().findViewById(R.id.startDaySelectionSpinner);
        endDaySelectionSpinner = getView().findViewById(R.id.endDaySelectionSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.days)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startDaySelectionSpinner.setAdapter(spinnerArrayAdapter);
        startDaySelectionSpinner.setOnItemSelectedListener(this);
        endDaySelectionSpinner.setAdapter(spinnerArrayAdapter);
        endDaySelectionSpinner.setOnItemSelectedListener(this);
        attendanceObligationValueTV = getView().findViewById(R.id.attendanceObligationValue_TV);
        attendanceObligationSeekBar = getView().findViewById(R.id.attendanceObligationSeekBar);
        attendanceObligationSeekBar.setOnSeekBarChangeListener(this);
        attendanceObligationValueTV.setText(attendanceObligationSeekBar.getProgress() + "%");
        semesterSelectionSpinner = getView().findViewById(R.id.courseCreationSemesterSelectionSpinner);
        semesterSelectionSpinner.setOnItemSelectedListener(this);
        ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters());
        semesterSelectionSpinner.setAdapter(adapter);
        ImageView closeIcon = getView().findViewById(R.id.courseCreationCloseIcon);
        closeIcon.setOnClickListener(this);
        schedule = new ArrayList<>();
        Button addScheduleButton = getView().findViewById(R.id.addScheduleButton);
        addScheduleButton.setOnClickListener(this);
        Button createButton = getView().findViewById(R.id.courseCreateButton);
        createButton.setOnClickListener(this);
        Button clearButton = getView().findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);
        if (BaseFragment.contextObject instanceof Semester) {
            Semester semester = (Semester) semesterSelectionSpinner.getSelectedItem();
            if (semester.getSemesterID() != ((Semester)BaseFragment.contextObject).getSemesterID()) {
                int count = semesterSelectionSpinner.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    semester = (Semester) semesterSelectionSpinner.getItemAtPosition(i);
                    if (((Semester)BaseFragment.contextObject).getSemesterID() == semester.getSemesterID()) {
                        semesterSelectionSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courseCreationCloseIcon:
                this.dismiss();
                break;
            case R.id.courseCreateButton:
                if(!checkInputValidity())
                    return;
                Course newCourse = new Course(courseName, selectedSemester, credit, attendanceObligation, schedule);
                selectedSemester.addCourse(getContext(), newCourse);
                newCourse.schedule(getContext());
                this.dismiss();
                if(mListener != null)
                    mListener.onAppContentOperation("semesterFragment", getString(R.string.course_created));
                break;
            case R.id.startTime_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime_ET.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute));
                                eHour = (hourOfDay + 1) % 24;
                                eMinute = minute;
                                endTime_ET.setText(String.format("%1$02d", eHour) + ":" + String.format("%1$02d", eMinute));
                            }
                        }, sHour, sMinute, true).show();
                break;
            case R.id.endTime_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTime_ET.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute));
                            }
                        }, eHour, eMinute, true).show();
                break;
            case R.id.addScheduleButton:
                String timeString = startTime_ET.getText().toString().trim();
                if (!RegexChecker.check(timeString, RegexChecker.clockPattern)) {
                    Toast.makeText(getContext(), getString(R.string.invalid_start_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] tokens = startTime_ET.getText().toString().split(":");
                sHour = Integer.parseInt(tokens[0]);
                sMinute = Integer.parseInt(tokens[1]);
                timeString = endTime_ET.getText().toString().trim();
                if (!RegexChecker.check(timeString, RegexChecker.clockPattern)) {
                    Toast.makeText(getContext(), getString(R.string.invalid_end_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                tokens = endTime_ET.getText().toString().split(":");
                eHour = Integer.parseInt(tokens[0]);
                eMinute = Integer.parseInt(tokens[1]);

                startTime.set(Calendar.HOUR_OF_DAY, sHour);
                startTime.set(Calendar.MINUTE, sMinute);
                endTime.set(Calendar.HOUR_OF_DAY, eHour);
                endTime.set(Calendar.MINUTE, eMinute);
                Calendar[] times = {startTime, endTime};
                schedule.add(times);
                getView().findViewById(R.id.scheduleLayout).setVisibility(View.VISIBLE);
                LinearLayout layout = getView().findViewById(R.id.scheduleInsideLayout);
                TextView tv = new TextView(getContext());
                tv.setText(startDaySelectionSpinner.getSelectedItem().toString()+ "\n" + sHour + ":" + sMinute + " - "+ eHour + ":" + eMinute);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tv);
                startTime = (Calendar) startTime.clone(); // Create new objects to avoid overwriting previous ones
                endTime = (Calendar) endTime.clone();
                break;
            case R.id.clearButton:
                schedule.clear();
                layout = getView().findViewById(R.id.scheduleInsideLayout);
                if(layout.getChildCount() > 0)
                    layout.removeAllViews();
                getView().findViewById(R.id.scheduleLayout).setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private boolean checkInputValidity() {
        courseName = courseName_ET.getText().toString().trim();
        if (courseName.length() < 1) {
            Toast.makeText(getContext(), getString(R.string.invalid_course_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            credit = Integer.parseInt(creditET.getText().toString().trim());
        }catch (NumberFormatException ex) {
            Toast.makeText(getContext(), getString(R.string.invalid_credit), Toast.LENGTH_SHORT).show();
            return false;
        }
        attendanceObligation = (float)(attendanceObligationSeekBar.getProgress() / 100.0);
        if (selectedSemester == null) {
            Toast.makeText(getContext(), getString(R.string.invalid_semester), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (schedule.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.empty_schedule_warning), Toast.LENGTH_SHORT).show();
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
            if (selectedSemester != adapterView.getSelectedItem())
                selectedSemester = (Semester) adapterView.getSelectedItem();
        }
        else if (adapterView == startDaySelectionSpinner) {
            int day;
            day = (adapterView.getSelectedItemPosition() + 2) % 7;
            startTime.set(Calendar.DAY_OF_WEEK, day);
            endDaySelectionSpinner.setSelection(adapterView.getSelectedItemPosition());
        }
        else if (adapterView == endDaySelectionSpinner) {
            int day;
            day = (adapterView.getSelectedItemPosition() + 2) % 7;
            endTime.set(Calendar.DAY_OF_WEEK, day);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (adapterView == semesterSelectionSpinner)
            selectedSemester = null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        attendanceObligationValueTV.setText(i + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
