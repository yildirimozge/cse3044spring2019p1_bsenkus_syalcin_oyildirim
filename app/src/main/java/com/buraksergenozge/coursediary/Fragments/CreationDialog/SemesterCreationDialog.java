package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.RegexChecker;

import java.util.Calendar;
import java.util.Objects;

public class SemesterCreationDialog extends CreationDialog {
    private String semesterName;
    private EditText nameEditText, startDateEditText, endDateEditText;
    private int sYear, sMonth, sDay, eYear, eMonth, eDay;
    private Calendar startDate, endDate;
    private Button createButton;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_semestercreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_semester));
        nameEditText = Objects.requireNonNull(getView()).findViewById(R.id.semesterNameEditText);
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
        startDateEditText.setText(getResources().getString(R.string.date_format, sDay, (sMonth + 1), sYear));
        startDateEditText.setOnClickListener(this);
        endDateEditText = getView().findViewById(R.id.endDateView);
        endDateEditText.setText(getResources().getString(R.string.date_format, eDay, (eMonth + 1), eYear));
        endDateEditText.setOnClickListener(this);
    }

    @Override
    protected void prepareSpinners() {
    }

    @Override
    protected void initializeEditMode() {
        appContent = MainScreen.activeAppContent;
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(((Semester)appContent).getName());
        nameEditText.setText(((Semester)appContent).getName());
        startDateEditText.setText(getResources().getString(R.string.date_format, ((Semester)appContent).getStartDate().get(Calendar.DAY_OF_MONTH), (((Semester)appContent).getStartDate().get(Calendar.MONTH) + 1), ((Semester)appContent).getStartDate().get(Calendar.YEAR)));
        endDateEditText.setText(getResources().getString(R.string.date_format, ((Semester)appContent).getEndDate().get(Calendar.DAY_OF_MONTH), (((Semester)appContent).getEndDate().get(Calendar.MONTH) + 1), ((Semester)appContent).getEndDate().get(Calendar.YEAR)));
        createButton.setText(getString(R.string.save));
    }

    @Override
    protected void initializeInfoMode() {
        appContent = MainScreen.activeAppContent;
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.creationTitle)).setText(((Semester)appContent).getName());
        nameEditText.setText(((Semester)appContent).getName());
        nameEditText.setEnabled(false);
        startDateEditText.setText(getResources().getString(R.string.date_format, ((Semester)appContent).getStartDate().get(Calendar.DAY_OF_MONTH), (((Semester)appContent).getStartDate().get(Calendar.MONTH) + 1), ((Semester)appContent).getStartDate().get(Calendar.YEAR)));
        startDateEditText.setEnabled(false);
        endDateEditText.setText(getResources().getString(R.string.date_format, ((Semester)appContent).getEndDate().get(Calendar.DAY_OF_MONTH), (((Semester)appContent).getEndDate().get(Calendar.MONTH) + 1), ((Semester)appContent).getEndDate().get(Calendar.YEAR)));
        endDateEditText.setEnabled(false);
        createButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.semesterCreateButton:
                if (checkInputValidity()) {
                    if (mode == EDIT_MODE) {
                        ((Semester)appContent).setName(semesterName);
                        ((Semester)appContent).setStartDate(startDate);
                        ((Semester)appContent).setEndDate(endDate);
                        appContent.updateOperation((MainScreen) getActivity());
                    }
                    else {
                        appContent = new Semester(semesterName, startDate, endDate);
                        appContent.addOperation((MainScreen)getActivity());
                    }
                    this.dismiss();
                    mListener.updateViewsOfAppContent(appContent);
                    MainScreen.showSnackbarMessage(Objects.requireNonNull(getActivity()).getWindow().getDecorView(), getString(appContent.getSaveMessage()));
                }
                break;
            case R.id.startDateView:
                new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateEditText.setText(getResources().getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), year));
                    }
                }, sYear, sMonth, sDay).show();
                break;
            case R.id.endDateView:
                new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateEditText.setText(getResources().getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), year));
                    }
                }, eYear, eMonth, eDay).show();
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
        if (sDay < 1 || sDay > 31) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
            return false;
        }
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
        if (eDay < 1 || eDay > 31) {
            Toast.makeText(getContext(), getString(R.string.invalid_end_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        endDate.set(eYear, eMonth - 1, eDay, 23, 59);
        if (startDate.after(endDate)) {
            Toast.makeText(getContext(), getString(R.string.end_date_before), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}