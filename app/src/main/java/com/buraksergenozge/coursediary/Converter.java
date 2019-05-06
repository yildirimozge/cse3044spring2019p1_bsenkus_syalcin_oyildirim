package com.buraksergenozge.coursediary;

import android.arch.persistence.room.TypeConverter;

import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Grade;
import com.buraksergenozge.coursediary.Data.GradingSystem;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Semester;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Converter {
    @TypeConverter
    public static Semester longToSemester(long semesterID) {
        return CourseDiaryDB.getDBInstance(null).semesterDAO().find(semesterID);
    }

    @TypeConverter
    public static long semesterToLong(Semester semester) {
        return semester.getSemesterID();
    }

    @TypeConverter
    public static List<Semester> stringToSemesterList(String semesterString) {
        List<Semester> semesters = new ArrayList<>();
        String[] semesterIDs = semesterString.split(";");
        for (String id: semesterIDs) {
            semesters.add(CourseDiaryDB.getDBInstance(null).semesterDAO().find(Long.parseLong(id)));
        }
        return semesters;
    }

    @TypeConverter
    public static String semesterListToString(List<Semester> semesters) {
        String semesterString = "";
        for (Semester semester: semesters) {
            semesterString += semester.getSemesterID() + ";";
        }
        return semesterString.substring(0,semesterString.length()-1);
    }

    @TypeConverter
    public static Course longToCourse(long courseID) {
        return CourseDiaryDB.getDBInstance(null).courseDAO().find(courseID);
    }

    @TypeConverter
    public static long courseToLong(Course course) {
        return course.getCourseID();
    }

    @TypeConverter
    public static CourseHour longToCourseHour(long courseHourID) {
        return CourseDiaryDB.getDBInstance(null).courseHourDAO().find(courseHourID);
    }

    @TypeConverter
    public static long courseHourToLong(CourseHour courseHour) {
        return courseHour.getCourseHourID();
    }

    @TypeConverter
    public static Note longToNote(long noteID) {
        return CourseDiaryDB.getDBInstance(null).noteDAO().find(noteID);
    }

    @TypeConverter
    public static long noteToLong(Note note) {
        return note.getNoteID();
    }

    @TypeConverter
    public static GradingSystem longToGradingSystem(long gradingSystemID) {
        return CourseDiaryDB.getDBInstance(null).gradingSystemDAO().find(gradingSystemID);
    }

    @TypeConverter
    public static long gradingSystemToLong(GradingSystem gradingSystem) {
        return gradingSystem.getGradingSystemID();
    }

    @TypeConverter
    public static Grade longToGrade(long gradeID) {
        return CourseDiaryDB.getDBInstance(null).gradeDAO().find(gradeID);
    }

    @TypeConverter
    public static long gradeToLong(Grade grade) {
        return grade.getGradeID();
    }

    @TypeConverter
    public static Calendar stringToCalendar(String calendarString) {
        Calendar calendar = Calendar.getInstance();
        String[] tokens = calendarString.split(";");
        calendar.set(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
        return calendar;
    }

    @TypeConverter
    public static String calendarToString(Calendar calendar) {
        String calendarString = calendar.get(Calendar.YEAR) + ";" + calendar.get(Calendar.MONTH) + ";" + calendar.get(Calendar.DAY_OF_MONTH) + ";" + calendar.get(Calendar.HOUR_OF_DAY) + ";" + calendar.get(Calendar.MINUTE);
        return calendarString;
    }

    @TypeConverter
    public static Map<Integer, Calendar[]> stringToSchedule(String scheduleString) {
        Map<Integer, Calendar[]> schedule = new HashMap<>();
        String[] days = scheduleString.split(";");
        String[] day;
        for (String currentDay: days) {
            day = currentDay.split(",");
            Calendar startHour = Calendar.getInstance();
            String[] clockTokens = day[1].split("\\.");
            startHour.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clockTokens[0]));
            startHour.set(Calendar.MINUTE, Integer.parseInt(clockTokens[1]));
            Calendar endHour = Calendar.getInstance();
            clockTokens = day[2].split("\\.");
            endHour.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clockTokens[0]));
            endHour.set(Calendar.MINUTE, Integer.parseInt(clockTokens[1]));
            Calendar[] clocks = {startHour, endHour};
            schedule.put(Integer.parseInt(day[0]), clocks);
        }
        return schedule;
    }

    @TypeConverter
    public static String scheduleToString(Map<Integer, Calendar[]> schedule) {
        String scheduleString = "";
        Iterator it = schedule.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Calendar[] clocks = (Calendar[]) pair.getValue();
            String startClockString = clocks[0].get(Calendar.HOUR_OF_DAY) + "." + clocks[0].get(Calendar.MINUTE);
            String endClockString = clocks[1].get(Calendar.HOUR_OF_DAY) + "." + clocks[1].get(Calendar.MINUTE);
            scheduleString += pair.getKey() + "," + startClockString + "," + endClockString + ";";
            it.remove(); // avoids a ConcurrentModificationException
        }
        return scheduleString.substring(0,scheduleString.length()-1);
    }
}