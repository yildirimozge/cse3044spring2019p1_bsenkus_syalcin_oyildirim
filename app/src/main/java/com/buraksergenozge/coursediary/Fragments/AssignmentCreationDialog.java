package com.buraksergenozge.coursediary.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.R;

import java.util.Calendar;

public class AssignmentCreationDialog extends CreationDialog implements View.OnClickListener {
    private Button createButton;
    private EditText nameEditText, deadlineEditText;
    private ImageView closeIcon;
    private int year, month, day;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.dialogfragment_assignmentcreation;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onResume() {
        super.onResume();
        nameEditText = getView().findViewById(R.id.semesterNameEditText);
        closeIcon = getView().findViewById(R.id.assignmentCreationCloseIcon);
        closeIcon.setOnClickListener(this);
        createButton = getView().findViewById(R.id.assignmentCreateButton);
        createButton.setOnClickListener(this);
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
                Calendar deadline = Calendar.getInstance();
                deadline.set(year, month, day);
               // Assignment newAssignment = new Assignment("", deadline);
               // Course.addAssignment(getContext(), newAssignment);
                this.dismiss();
                if(mListener != null)
                    mListener.onAssignmentAdded();
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onAssignmentAdded();
    }
}