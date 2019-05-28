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

import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseCreationDialog extends CreationDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    private EditText nameET, creditET, startTime_ET, endTime_ET;
    private TextView attendanceObligationValueTV;
    private SeekBar attendanceObligationSeekBar;
    private Spinner semesterSelectionSpinner, daySelectionSpinner;
    private Semester selectedSemester;
    private Calendar startTime, endTime;
    private int sHour, sMinute, eHour, eMinute;
    List<Calendar[]> schedule;
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
        nameET = getView().findViewById(R.id.courseName_ET);
        creditET = getView().findViewById(R.id.credit_ET);
        startTime_ET = getView().findViewById(R.id.startTime_ET);
        startTime_ET.setOnClickListener(this);
        endTime_ET = getView().findViewById(R.id.endTime_ET);
        endTime_ET.setOnClickListener(this);
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        sHour = calendar.get(Calendar.HOUR_OF_DAY);
        sMinute = calendar.get(Calendar.MINUTE);
        startTime_ET.setText(String.format("%1$02d", sHour) + ":" + String.format("%1$02d", sMinute));
        calendar.roll(Calendar.HOUR_OF_DAY, 1);
        eHour = calendar.get(Calendar.HOUR_OF_DAY);
        eMinute = sMinute;
        endTime_ET.setText(String.format("%1$02d", eHour) + ":" + String.format("%1$02d", eMinute));
        startTime.set(Calendar.HOUR_OF_DAY, sHour);
        startTime.set(Calendar.MINUTE, sMinute);
        endTime.set(Calendar.HOUR_OF_DAY, eHour);
        endTime.set(Calendar.MINUTE, eMinute);
        daySelectionSpinner = getView().findViewById(R.id.daySelectionSpinner);
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, days); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelectionSpinner.setAdapter(spinnerArrayAdapter);
        daySelectionSpinner.setOnItemSelectedListener(this);
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
        Button addScheduleButton = getView().findViewById(R.id.addScheduleButton);
        addScheduleButton.setOnClickListener(this);
        Button createButton = getView().findViewById(R.id.courseCreateButton);
        createButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courseCreationCloseIcon:
                this.dismiss();
                break;
            case R.id.courseCreateButton:
                // TODO: Hata kontrolü yap.
                String name = nameET.getText().toString();
                int credit = Integer.parseInt(creditET.getText().toString());
                float attendanceObligation = (float)(attendanceObligationSeekBar.getProgress() / 100.0);
                Course newCourse = new Course(name, selectedSemester, credit, attendanceObligation, schedule);
                selectedSemester.addCourse(getContext(), newCourse);
                this.dismiss();
                if(mListener != null)
                    mListener.onCourseOperation(getString(R.string.course_created));
                break;
            case R.id.startTime_ET:
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime_ET.setText(String.format("%1$02d", hourOfDay) + ":" + String.format("%1$02d", minute));
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
                schedule = new ArrayList<>();
                String[] tokens = startTime_ET.getText().toString().split(":");
                sHour = Integer.parseInt(tokens[0]);
                sMinute = Integer.parseInt(tokens[1]);
                tokens = endTime_ET.getText().toString().split(":");
                eHour = Integer.parseInt(tokens[0]);
                eMinute = Integer.parseInt(tokens[1]);
                startTime.set(Calendar.HOUR_OF_DAY, sHour);
                startTime.set(Calendar.MINUTE, sMinute);
                endTime.set(Calendar.HOUR_OF_DAY, eHour);
                endTime.set(Calendar.MINUTE, eMinute);
                Calendar[] times = {startTime, endTime};
                schedule.add(times);

                getView().findViewById(R.id.scheduleScrollView).setVisibility(View.VISIBLE);

                LinearLayout ll = getView().findViewById(R.id.scheduleInsideLayout);
                TextView tv = new TextView(getContext());
                tv.setText(daySelectionSpinner.getSelectedItem().toString()+ "\n" + sHour + ":" + sMinute + " - "+ eHour + ":" + eMinute);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.addView(tv);
                break;
            default:
                break;
        }
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
        }
        else if (adapterView == daySelectionSpinner) {
            int day;
            day = (adapterView.getSelectedItemPosition() + 2) % 7;
            startTime.set(Calendar.DAY_OF_WEEK, day);
            endTime.set(Calendar.DAY_OF_WEEK, day);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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

    public interface OnFragmentInteractionListener {
        void onCourseOperation(String message);
    }
}