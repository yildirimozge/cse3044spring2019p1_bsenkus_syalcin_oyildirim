package com.buraksergenozge.coursediary.Data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static List<Semester> semesters = new ArrayList<>();

    private User() {
    }

    public static List<Semester> getSemesters() {
        return semesters;
    }

    public static void setSemesters(List<Semester> semesters) {
        User.semesters = semesters;
    }

    public static void addSemester(Context context, Semester semester) {
        long semesterID = CourseDiaryDB.getDBInstance(context).semesterDAO().addSemester(semester);
        semester.setSemesterID(semesterID);
        semesters.add(semester);

    }

    public static void deleteSemester(Context context, Semester semester) {
        semesters.remove(semester);
        CourseDiaryDB.getDBInstance(context).semesterDAO().deleteSemester(semester);
    }

    public static Semester findSemesterByID(long semesterID) {
        for (Semester semester:semesters) {
            if (semester.getSemesterID() == semesterID)
                return semester;
        }
        return null;
    }

    public static Course findCourseByID(long courseID) {
        for (Semester semester:semesters) {
            for (Course course: semester.getCourses()) {
                if (course.getCourseID() == courseID)
                    return course;
            }
        }
        return null;
    }

    public static CourseHour findCourseHourByID(long courseHourID) {
        for (Semester semester:semesters) {
            for (Course course: semester.getCourses()) {
                for (CourseHour courseHour: course.getCourseHours()) {
                    if (courseHour.getCourseHourID() == courseHourID)
                        return courseHour;
                }
            }
        }
        return null;
    }

    public static Assignment findAssignmentByID(long assignmentID) {
        for (Semester semester:semesters) {
            for (Course course: semester.getCourses()) {
                for (Assignment assignment: course.getAssignments()) {
                    if (assignment.getAssignmentID() == assignmentID)
                        return assignment;
                }
            }
        }
        return null;
    }

    public static List<Semester> integrateWithDB(Context context) { // Get latest data from DB
        semesters = CourseDiaryDB.getDBInstance(context).semesterDAO().getAll();
        return semesters;
    }
}
