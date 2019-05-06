package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.buraksergenozge.coursediary.Converter;
import com.buraksergenozge.coursediary.DAO.AssignmentDAO;
import com.buraksergenozge.coursediary.DAO.CourseDAO;
import com.buraksergenozge.coursediary.DAO.CourseHourDAO;
import com.buraksergenozge.coursediary.DAO.GradeDAO;
import com.buraksergenozge.coursediary.DAO.GradingSystemDAO;
import com.buraksergenozge.coursediary.DAO.NoteDAO;
import com.buraksergenozge.coursediary.DAO.SemesterDAO;

@Database(entities = {Semester.class, Course.class, CourseHour.class, Note.class, GradingSystem.class, Grade.class, Assignment.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class CourseDiaryDB extends RoomDatabase {
    public abstract SemesterDAO semesterDAO();
    public abstract CourseDAO courseDAO();
    public abstract CourseHourDAO courseHourDAO();
    public abstract NoteDAO noteDAO();
    public abstract GradingSystemDAO gradingSystemDAO();
    public abstract GradeDAO gradeDAO();
    public abstract AssignmentDAO assignmentDAO();

    private static CourseDiaryDB INSTANCE;

    public static CourseDiaryDB getDBInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CourseDiaryDB.class, "user-database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public  static  void destroyInstance(){
        INSTANCE = null;
    }
}