package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;

import java.util.List;

@Dao
public interface SemesterDAO {
    @Insert
    long addSemester(Semester semester);

    @Delete
    void deleteSemester(Semester semester);

    @Query("DELETE FROM Semester")
    void deleteAll();

    @Query("DELETE FROM Course WHERE semester = :semester")
    void deleteAllCoursesOfSemester(Semester semester);

    @Query("SELECT * FROM Semester")
    List<Semester> getAll();

    @Query("SELECT * FROM Course WHERE semester = :semester")
    List<Course> getAllCoursesOfSemester(Semester semester);

    @Query("SELECT * FROM Semester where semesterID = :semesterID")
    Semester find(long semesterID);

    @Update
    void update(Semester semester);
}