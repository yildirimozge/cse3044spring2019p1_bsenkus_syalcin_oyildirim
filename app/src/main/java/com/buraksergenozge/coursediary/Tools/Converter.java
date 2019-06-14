package com.buraksergenozge.coursediary.Tools;

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
import java.util.List;

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
        if (gradingSystemID == -1)
            return null;
        return CourseDiaryDB.getDBInstance(null).gradingSystemDAO().find(gradingSystemID);
    }

    @TypeConverter
    public static long gradingSystemToLong(GradingSystem gradingSystem) {
        if (gradingSystem == null)
            return -1;
        return gradingSystem.getGradingSystemID();
    }

    @TypeConverter
    public static Grade longToGrade(long gradeID) {
        if (gradeID == -1)
            return null;
        return CourseDiaryDB.getDBInstance(null).gradeDAO().find(gradeID);
    }

    @TypeConverter
    public static long gradeToLong(Grade grade) {
        if (grade == null)
            return -1;
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
        return calendar.get(Calendar.YEAR) + ";" + calendar.get(Calendar.MONTH) + ";" + calendar.get(Calendar.DAY_OF_MONTH) + ";" + calendar.get(Calendar.HOUR_OF_DAY) + ";" + calendar.get(Calendar.MINUTE);
    }

    @TypeConverter // scheduleString=2.9.30,2.11.30;4.10.30,4.11.30 means Monday 9.30-11.30 and Wednesday 10.30-11.30
    public static List<Calendar[]> stringToSchedule(String scheduleString) { // scheduleString = 2.9.30,2.11.30;4.10.30,4.11.30
        List<Calendar[]> schedule = new ArrayList<>();
        String[] days = scheduleString.split(";"); // days = {(2.9.30,2.11.30), (4.10.30,4.11.30)}
        String[] day;
        for (String currentDay: days) {
            day = currentDay.split(","); // day = {(2.9.30), (2.11.30)}
            Calendar startTime = Calendar.getInstance();
            String[] clockTokens = day[0].split("\\."); // clockTokens = {2, 9, 300}
            startTime.set(Calendar.DAY_OF_WEEK, Integer.parseInt(clockTokens[0])); // startTime.DAY_OF_WEEK = 2 (Monday)
            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clockTokens[1])); // startTime.HOUR_OF_DAY = 9
            startTime.set(Calendar.MINUTE, Integer.parseInt(clockTokens[2])); // startTime.MINUTE = 30
            Calendar endTime = Calendar.getInstance();
            clockTokens = day[1].split("\\.");
            endTime.set(Calendar.DAY_OF_WEEK, Integer.parseInt(clockTokens[0]));
            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clockTokens[1]));
            endTime.set(Calendar.MINUTE, Integer.parseInt(clockTokens[2]));
            Calendar[] clocks = {startTime, endTime};
            schedule.add(clocks);
        }
        return schedule;
    }

    @TypeConverter
    public static String scheduleToString(List<Calendar[]> schedule) {
        StringBuilder scheduleString = new StringBuilder();
        for (Calendar[] entry: schedule) {
            Calendar startTime = entry[0];
            Calendar endTime = entry[1];
            String startClockString = startTime.get(Calendar.DAY_OF_WEEK) + "." + startTime.get(Calendar.HOUR_OF_DAY) + "." + startTime.get(Calendar.MINUTE);
            String endClockString = endTime.get(Calendar.DAY_OF_WEEK) + "." + endTime.get(Calendar.HOUR_OF_DAY) + "." + endTime.get(Calendar.MINUTE);
            scheduleString.append(startClockString).append(",").append(endClockString).append(";");
        }
        return scheduleString.substring(0,scheduleString.length()-1);
    }
}