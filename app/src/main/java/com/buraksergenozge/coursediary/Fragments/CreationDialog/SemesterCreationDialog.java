package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.RegexChecker;

import java.util.Calendar;

public class SemesterCreationDialog extends CreationDialog implements View.OnClickListener {
    private String semesterName;
    private Button createButton;
    private EditText nameEditText, startDateEditText, endDateEditText;
    private ImageView closeIcon;
    private int sYear, sMonth, sDay, eYear, eMonth, eDay;
    private Calendar startDate, endDate;


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
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
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
                if (!checkInputValidity())
                    return;
                Semester newSemester = new Semester(semesterName, startDate, endDate);
                User.addSemester(getContext(), newSemester);
                this.dismiss();
                if(mListener != null)
                    mListener.onAppContentOperation("archiveFragment", getString(R.string.semester_created));
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

    private boolean checkInputValidity() {
        semesterName = nameEditText.getText().toString().trim();
        if (semesterName.length() < 1) {
            Toast.makeText(getContext(), getString(R.string.invalid_semester_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        String dateString = startDateEditText.getText().toString().trim();
        if (!RegexChecker.check(dateString, RegexChecker.datePattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_start_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] tokens = dateString.split("-");
        sDay = Integer.parseInt(tokens[0]);
        sMonth = Integer.parseInt(tokens[1]);
        sYear = Integer.parseInt(tokens[2]);
        startDate.set(sYear, sMonth - 1, sDay);
        dateString = endDateEditText.getText().toString().trim();
        if (!RegexChecker.check(dateString, RegexChecker.datePattern)) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        tokens = dateString.split("-");
        eDay = Integer.parseInt(tokens[0]);
        eMonth = Integer.parseInt(tokens[1]);
        eYear = Integer.parseInt(tokens[2]);
        endDate.set(eYear, eMonth - 1, eDay, 23, 59);

        if (!endDate.after(Calendar.getInstance())) {
            Toast.makeText(getContext(), getString(R.string.end_date_past), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
