package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import java.util.List;

@Entity
public class GradingSystem {
    @PrimaryKey(autoGenerate = true)
    private long gradingSystemID;
    @ColumnInfo
    private String name;
    @Ignore
    private List<Grade> gradeList;

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

    public void addGrade(Context context, Grade grade) {
        gradeList.add(grade);
        CourseDiaryDB.getDBInstance(context).gradeDAO().addGrade(grade);
    }

    public void deleteGrade(Context context, Grade grade) {
        gradeList.remove(grade);
        CourseDiaryDB.getDBInstance(context).gradeDAO().deleteGrade(grade);
    }
}