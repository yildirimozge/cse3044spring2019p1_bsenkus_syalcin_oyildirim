package com.buraksergenozge.coursediary.Fragments.CreationDialog;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
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
    private Button addGradeButton, clearButton;
    private final List<Grade> grades = new ArrayList<>();
    private final List<String> codes = new ArrayList<>();
    private final List<Float> coefficients = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.dialogfragment_gradingsystemcreation;
    }

    @Override
    protected void initializeViews() {
        super.initializeViews();
        toolbarTitle_TV.setText(getString(R.string.new_grading_system));
        createButton = Objects.requireNonNull(getView()).findViewById(R.id.gradingSystemCreateButton);
        createButton.setOnClickListener(this);
        name_ET = getView().findViewById(R.id.gradingSystemNameEditText);
        addGradeButton = getView().findViewById(R.id.addGradeButton);
        addGradeButton.setOnClickListener(this);
        clearButton = getView().findViewById(R.id.gradesClearButton);
        clearButton.setOnClickListener(this);
        gradeCode_ET = getView().findViewById(R.id.grade_code_ET);
        gradeCoefficient_ET = getView().findViewById(R.id.grade_coefficient_ET);
    }

    @Override
    protected void prepareSpinners() {
    }

    @Override
    protected void initializeEditMode() {
        super.initializeEditMode();
        name_ET.setText(((GradingSystem) appContent).getName());
        Objects.requireNonNull(getView()).findViewById(R.id.gradesLayout).setVisibility(View.VISIBLE);
        LinearLayout layout = getView().findViewById(R.id.gradesInsideLayout);
        for (Grade grade: ((GradingSystem)appContent).getGradeList()) {
            codes.add(grade.getCode());
            coefficients.add(grade.getCoefficient());
            TextView tv = new TextView(getContext());
            tv.setText(grade.getCode() + "\n" + grade.getCoefficient());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(tv);
        }
    }

    @Override
    protected void initializeInfoMode() {
        super.initializeInfoMode();
        name_ET.setText(((GradingSystem) appContent).getName());
        name_ET.setEnabled(false);
        Objects.requireNonNull(getView()).findViewById(R.id.gradesLayout).setVisibility(View.VISIBLE);
        LinearLayout layout = getView().findViewById(R.id.gradesInsideLayout);
        for (Grade grade: grades) {
            TextView tv = new TextView(getContext());
            tv.setText(grade.getCode() + "\n" + grade.getCoefficient());
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(tv);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.gradingSystemCreateButton:
                if (checkInputValidity()) {
                    if (mode == EDIT_MODE) {
                        ((GradingSystem)appContent).setName(name);
                        for (int i = 0; i < codes.size(); i++) {
                            grades.add(new Grade((GradingSystem) appContent, codes.get(i), coefficients.get(i))) ;
                        }
                        ((GradingSystem) appContent).setGradeList(grades);
                        appContent.updateOperation((MainScreen) getActivity());
                    }
                    else {
                        appContent = new GradingSystem(name);
                        for (int i = 0; i < codes.size(); i++) {
                            grades.add(new Grade((GradingSystem) appContent, codes.get(i), coefficients.get(i))) ;
                        }
                        ((GradingSystem) appContent).setGradeList(grades);
                        appContent.create((MainScreen) getActivity());
                    }
                    this.dismiss();
                }
                break;
            case R.id.addGradeButton:
                String code = gradeCode_ET.getText().toString().trim();
                if (code.length() == 0) {
                    Toast.makeText(getContext(), getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                String coefficientString = gradeCoefficient_ET.getText().toString().trim();
                if (!RegexChecker.check(coefficientString, RegexChecker.floatPattern)) {
                    Toast.makeText(getContext(), getString(R.string.invalid_coefficient), Toast.LENGTH_SHORT).show();
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
                codes.clear();
                coefficients.clear();
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
            Toast.makeText(getContext(), getString(R.string.invalid_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (codes.size() < 1) {
            Toast.makeText(getContext(), getString(R.string.no_grade), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}