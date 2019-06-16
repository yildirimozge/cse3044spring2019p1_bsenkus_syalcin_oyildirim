package com.buraksergenozge.coursediary.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class SettingsFragment extends DialogFragment implements View.OnClickListener {
    private Spinner remindAssignmentsTimeSpinner;
    private EditText remindAssignmentsValue_ET;
    public static final String tag = "settingsFragment";
    public static final String ASSIGNMENT_REMIND_VALUE = "assignmentRemindValue";
    private static int assignmentRemindValue;
    public static final String ASSIGNMENT_REMIND_TIME_TYPE = "assignmentRemindTimeType";
    private static int assignmentRemindTimeType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
        remindAssignmentsValue_ET = Objects.requireNonNull(getView()).findViewById(R.id.remind_assignments_ET);
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        assignmentRemindValue = sharedPref.getInt(ASSIGNMENT_REMIND_VALUE, 1);
        remindAssignmentsValue_ET.setText(getResources().getString(R.string.int_holder, assignmentRemindValue));
        remindAssignmentsTimeSpinner = getView().findViewById(R.id.remind_assignments_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.settings_time));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindAssignmentsTimeSpinner.setAdapter(spinnerArrayAdapter);
        assignmentRemindTimeType = sharedPref.getInt(ASSIGNMENT_REMIND_TIME_TYPE, 0);
        remindAssignmentsTimeSpinner.setSelection(assignmentRemindTimeType);
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.settings));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.creationCloseIcon) {
            dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        String valueString = remindAssignmentsValue_ET.getText().toString();
        if (valueString.equals("") || valueString.length() > 2) {
            assignmentRemindValue = 1;
            Toast.makeText(getContext(), getString(R.string.assignment_reminder_value_warning), Toast.LENGTH_SHORT).show();
        }
        else
            assignmentRemindValue = Integer.parseInt(valueString);
        assignmentRemindTimeType = remindAssignmentsTimeSpinner.getSelectedItemPosition();
        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ASSIGNMENT_REMIND_VALUE, assignmentRemindValue);
        editor.putInt(ASSIGNMENT_REMIND_TIME_TYPE, assignmentRemindTimeType);
        editor.apply();
        for (Assignment assignment: User.getActiveAssignments())
            assignment.updateNotificationTime((AppCompatActivity) getActivity(), false);
    }
}