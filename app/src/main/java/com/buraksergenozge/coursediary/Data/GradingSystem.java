package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.GradingSystemCreationDialog;
import com.buraksergenozge.coursediary.R;

import java.util.List;

@Entity
public class GradingSystem extends AppContent {
    @PrimaryKey(autoGenerate = true)
    private long gradingSystemID;
    @ColumnInfo
    private String name;
    @Ignore
    private List<Grade> gradeList;

    public GradingSystem(String name) {
        this.name = name;
    }

    public long getGradingSystemID() {
        return gradingSystemID;
    }

    public void setGradingSystemID(long gradingSystemID) {
        this.gradingSystemID = gradingSystemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public static CreationDialog getCreationDialog(int mode) {
        CreationDialog creationDialog = new GradingSystemCreationDialog();
        creationDialog.mode = mode;
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.EDIT_MODE));
    }

    public void integrateWithDB(Context context) {
        gradeList = CourseDiaryDB.getDBInstance(context).gradingSystemDAO().getAllGradesOfGradingSystem(this);
    }

    @Override
    public int getSaveMessage() {
        return R.string.grading_system_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        this.gradingSystemID = CourseDiaryDB.getDBInstance(activity).gradingSystemDAO().addGradingSystem(this);
        for (Grade grade: gradeList) {
            long gradeID = CourseDiaryDB.getDBInstance(activity).gradeDAO().addGrade(grade);
            grade.setGradeID(gradeID);
        }
        User.getGradingSystems().add(this);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.grading_system_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        User.getGradingSystems().remove(this);
        CourseDiaryDB.getDBInstance(activity).gradingSystemDAO().deleteGradingSystem(this);
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
        CourseDiaryDB.getDBInstance(activity).gradingSystemDAO().deleteAllGradesOfGradingSystem(this);
        CourseDiaryDB.getDBInstance(activity).gradingSystemDAO().update(this);
        for (Grade grade: gradeList) {
            CourseDiaryDB.getDBInstance(activity).gradeDAO().addGrade(grade);
        }
    }

    @Override
    public void showInfo(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.INFO_MODE));
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return new String[0];
    }

    @Override
    @NonNull
    public String toString() {
        return name;
    }
}