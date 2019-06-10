package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Grade;
import com.buraksergenozge.coursediary.Data.GradingSystem;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.RegexChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GradingSystemCreationDialog extends CreationDialog {
    private String name;
    private EditText name_ET, gradeCode_ET, gradeCoefficient_ET;
    private Button createButton, addGradeButton;
    private List<Grade> grades = new ArrayList<>();
    private List<String> codes = new ArrayList<>();
    private List<Float> coefficients = new ArrayList<>();
    private AppContent appContent = null;

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_gradingsystemcreation;
    }

    @Override
    protected void initializeViews() {
        ImageView closeIcon = Objects.requireNonNull(getView()).findViewById(R.id.creationCloseIcon);
        closeIcon.setOnClickListener(this);
        ((TextView)getView().findViewById(R.id.creationTitle)).setText(getString(R.string.new_grading_system));
        createButton = Objects.requireNonNull(getView()).findViewById(R.id.gradingSystemCreateButton);
        createButton.setOnClickListener(this);
        name_ET = getView().findViewById(R.id.gradingSystemNameEditText);
        addGradeButton = getView().findViewById(R.id.addGradeButton);
        addGradeButton.setOnClickListener(this);
        gradeCode_ET = getView().findViewById(R.id.grade_code_ET);
        gradeCoefficient_ET = getView().findViewById(R.id.grade_coefficient_ET);
    }

    @Override
    protected void prepareSpinners() {
    }

    @Override
    protected void initializeEditMode() {
        appContent = MainScreen.activeAppContent;
        name_ET.setText(((GradingSystem) appContent).getName());
        createButton.setText(getString(R.string.save));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.gradingSystemCreateButton:
                if (checkInputValidity()) {
                    if (isEditMode) {
                        ((GradingSystem)appContent).setName(name);
                        appContent.updateOperation((MainScreen) getActivity());
                    }
                    else {
                        appContent = new GradingSystem(name);
                        for (int i = 0; i < codes.size(); i++) {
                            grades.add(new Grade((GradingSystem) appContent, codes.get(i), coefficients.get(i))) ;
                        }
                        ((GradingSystem) appContent).setGradeList(grades);
                        appContent.addOperation((MainScreen) getActivity());
                    }
                    this.dismiss();
                    mListener.updateViewsOfAppContent(appContent);
                    MainScreen.showSnackbarMessage(getView(), getString(appContent.getSaveMessage()));
                }
                break;
            case R.id.addGradeButton:
                String code = gradeCode_ET.getText().toString().trim();
                if (code.length() == 0) {
                    Toast.makeText(getContext(), "INVALID CODE", Toast.LENGTH_SHORT).show();
                    return;
                }
                String coefficientString = gradeCoefficient_ET.getText().toString().trim();
                if (!RegexChecker.check(coefficientString, RegexChecker.floatPattern)) {
                    Toast.makeText(getContext(), "INVALID COEFFICIENT", Toast.LENGTH_SHORT).show();
                    return;
                }
                codes.add(code);
                float coefficient = Float.parseFloat(coefficientString);
                coefficients.add(coefficient);
                Objects.requireNonNull(getView()).findViewById(R.id.gradesLayout).setVisibility(View.VISIBLE);
                LinearLayout layout = getView().findViewById(R.id.gradesInsideLayout);
                TextView tv = new TextView(getContext());
                tv.setText(code + "\n" + coefficient);
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tv);
                break;
            case R.id.gradesClearButton:
                grades.clear();
                layout = Objects.requireNonNull(getView()).findViewById(R.id.gradesInsideLayout);
                if(layout.getChildCount() > 0)
                    layout.removeAllViews();
                getView().findViewById(R.id.gradesLayout).setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private boolean checkInputValidity() {
        name = name_ET.getText().toString().trim();
        if (name.length() == 0) {
            Toast.makeText(getContext(), "INVALID NAME", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (grades.size() < 1) {
            Toast.makeText(getContext(), "ADD AT LEAST ONE GRADE", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}