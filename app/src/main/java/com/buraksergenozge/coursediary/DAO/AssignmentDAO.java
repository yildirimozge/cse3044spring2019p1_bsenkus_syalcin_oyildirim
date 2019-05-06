package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.Assignment;

import java.util.List;

@Dao
public interface AssignmentDAO {
    @Insert
    long addAssignment(Assignment assignment);

    @Delete
    void deleteAssignment(Assignment assignment);

    @Query("DELETE FROM Assignment")
    void deleteAll();

    @Query("SELECT * FROM Assignment")
    List<Assignment> getAll();

    @Query("SELECT * FROM Assignment where assignmentID = :assignmentID")
    Assignment find(long assignmentID);

    @Update
    void update(Assignment assignment);
}