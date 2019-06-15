package com.buraksergenozge.coursediary.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert
    long addCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("SELECT * FROM Course")
    List<Course> getAll();

    @Query("SELECT * FROM CourseHour WHERE course = :course")
    List<CourseHour> getAllCourseHoursOfCourse(Course course);

    @Query("SELECT * FROM Assignment WHERE course = :course")
    List<Assignment> getAllAssignmentsOfCourse(Course course);

    @Query("SELECT * FROM Course where courseID = :courseID")
    Course find(long courseID);

    @Update
    void update(Course course);
}