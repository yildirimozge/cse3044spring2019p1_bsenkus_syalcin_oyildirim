package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Note;

import java.util.List;

@Dao
public interface CourseHourDAO {
    @Insert
    long addCourseHour(CourseHour courseHour);

    @Insert
    void addCourseHourList(List<CourseHour> courseHours);

    @Delete
    void deleteCourseHour(CourseHour courseHour);

    @Query("DELETE FROM CourseHour")
    void deleteAll();

    @Query("DELETE FROM Note WHERE courseHour = :courseHour")
    void deleteAllNotesOfCourseHour(CourseHour courseHour);

    @Query("SELECT * FROM CourseHour")
    List<CourseHour> getAll();

    @Query("SELECT * FROM Note WHERE courseHour = :courseHour")
    List<Note> getAllNotesOfCourseHour(CourseHour courseHour);

    @Query("SELECT * FROM CourseHour where courseHourID = :courseHourID")
    CourseHour find(long courseHourID);

    @Update
    void update(CourseHour courseHour);
}