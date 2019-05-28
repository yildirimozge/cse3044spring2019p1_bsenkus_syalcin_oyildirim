package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

import java.util.Calendar;

public class SemesterCreationDialog extends CreationDialog implements View.OnClickListener {
    private Button createButton;
    private EditText nameEditText, startDateEditText, endDateEditText;
    private ImageView closeIcon;
    private int sYear, sMonth, sDay, eYear, eMonth, eDay;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.dialogfragment_semestercreation;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onResume() {
        super.onResume();
        nameEditText = getView().findViewById(R.id.semesterNameEditText);
        closeIcon = getView().findViewById(R.id.semesterCreationCloseIcon);
        closeIcon.setOnClickListener(this);
        createButton = getView().findViewById(R.id.semesterCreateButton);
        createButton.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        sYear = c.get(Calendar.YEAR);
        sMonth = c.get(Calendar.MONTH);
        sDay = c.get(Calendar.DAY_OF_MONTH);
        eYear = sYear;
        eMonth = sMonth;
        eDay = sDay;
        startDateEditText = getView().findViewById(R.id.startDateView);
        startDateEditText.setText(sDay + "-" + (sMonth + 1) + "-" + sYear);
        startDateEditText.setOnClickListener(this);
        endDateEditText = getView().findViewById(R.id.endDateView);
        endDateEditText.setText(sDay + "-" + (sMonth + 1) + "-" + sYear);
        endDateEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.semesterCreationCloseIcon:
                this.dismiss();
                break;
            case R.id.semesterCreateButton:
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.set(sYear, sMonth, sDay);
                end.set(eYear, eMonth, eDay);
                Semester newSemester = new Semester(nameEditText.getText().toString(), start, end);
                User.addSemester(getContext(), newSemester);
                this.dismiss();
                if(mListener != null)
                    mListener.onSemesterOperation(getString(R.string.semester_created));
                break;
            case R.id.startDateView:
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, sYear, sMonth, sDay).show();
                break;
            case R.id.endDateView:
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateEditText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, eYear, eMonth, eDay).show();
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

    public interface OnFragmentInteractionListener {
        void onSemesterOperation(String message);
    }
}