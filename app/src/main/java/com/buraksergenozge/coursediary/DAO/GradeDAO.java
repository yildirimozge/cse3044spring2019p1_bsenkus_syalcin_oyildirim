package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.Grade;

import java.util.List;

@Dao
public interface GradeDAO {
    @Insert
    long addGrade(Grade grade);

    @Delete
    void deleteGrade(Grade grade);

    @Query("SELECT * FROM Grade")
    List<Grade> getAll();

    @Query("SELECT * FROM Grade where gradeID = :gradeID")
    Grade find(long gradeID);

    @Update
    void update(Grade grade);
}