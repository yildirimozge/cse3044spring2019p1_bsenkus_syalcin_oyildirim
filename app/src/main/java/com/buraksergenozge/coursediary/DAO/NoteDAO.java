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
public interface NoteDAO {
    @Insert
    long addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM Note")
    void deleteAll();

    @Query("DELETE FROM Note WHERE courseHour = :courseHour")
    void deleteAllNotesOfCourseHour(CourseHour courseHour);

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @Query("SELECT * FROM Note WHERE courseHour = :courseHour")
    List<Note> getAllNotesOfCourseHour(CourseHour courseHour);

    @Query("SELECT * FROM Note where noteID = :noteID")
    Note find(long noteID);

    @Update
    void update(Note note);
}