package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = GradingSystem.class, parentColumns = "gradingSystemID", childColumns = "gradingSystem",
        onDelete = CASCADE))
public class Grade {
    @PrimaryKey(autoGenerate = true)
    private long gradeID;
    @ColumnInfo
    private GradingSystem gradingSystem;
    @ColumnInfo
    private String code;
    @ColumnInfo
    private float coefficient;

    public long getGradeID() {
        return gradeID;
    }

    public void setGradeID(long gradeID) {
        this.gradeID = gradeID;
    }

    public GradingSystem getGradingSystem() {
        return gradingSystem;
    }

    public void setGradingSystem(GradingSystem gradingSystemID) {
        this.gradingSystem = gradingSystemID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }
}