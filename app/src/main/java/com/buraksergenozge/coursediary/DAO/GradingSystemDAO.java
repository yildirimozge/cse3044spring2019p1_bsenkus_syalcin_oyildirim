package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.Grade;
import com.buraksergenozge.coursediary.Data.GradingSystem;

import java.util.List;

@Dao
public interface GradingSystemDAO {
    @Insert
    long addGradingSystem(GradingSystem gradingSystem);

    @Delete
    void deleteGradingSystem(GradingSystem gradingSystem);

    @Query("DELETE FROM GradingSystem")
    void deleteAll();

    @Query("SELECT * FROM GradingSystem")
    List<GradingSystem> getAll();

    @Query("SELECT * FROM Grade WHERE gradingSystem = :gradingSystem")
    List<Grade> getAllGradesOfGradingSystem(GradingSystem gradingSystem);

    @Query("SELECT * FROM GradingSystem where gradingSystemID = :gradingSystemID")
    GradingSystem find(long gradingSystemID);

    @Update
    void update(GradingSystem gradingSystem);
}