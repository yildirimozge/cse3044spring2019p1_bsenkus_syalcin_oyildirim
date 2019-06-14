package com.buraksergenozge.coursediary.Data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class User {
    private static List<Semester> semesters = new ArrayList<>();
    private static List<GradingSystem> gradingSystems = new ArrayList<>();

    private User() {
    }

    public static List<Semester> getSemesters() {
        return semesters;
    }

    public static List<GradingSystem> getGradingSystems() {
        return gradingSystems;
    }

    public static List<Assignment> getActiveAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                for (Assignment assignment: course.getAssignments()) {
                    if (assignment.getDeadline().after(Calendar.getInstance()))
                        assignments.add(assignment);
                }
            }
        }
        return assignments;
    }

    public static List<CourseHour> getUpcomingCourseHours() {
        List<CourseHour> courseHours = new ArrayList<>();
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                for (CourseHour courseHour: course.getCourseHours()) {
                    Calendar intervalStart = Calendar.getInstance();
                    intervalStart.add(Calendar.DATE, -2);
                    Calendar intervalEnd = Calendar.getInstance();
                    intervalEnd.add(Calendar.DATE, 7);
                    if (courseHour.getStartDate().after(intervalStart) && intervalEnd.after(courseHour.getStartDate()))
                        courseHours.add(courseHour);
                }
            }
        }
        Collections.sort(courseHours);
        return courseHours;
    }

    public static Semester findSemesterByID(long semesterID) {
        for (Semester semester: semesters) {
            if (semester.getSemesterID() == semesterID)
                return semester;
        }
        return null;
    }

    public static Course findCourseByID(long courseID) {
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                if (course.getCourseID() == courseID)
                    return course;
            }
        }
        return null;
    }

    public static Assignment findAssignmentByID(long assignmentID) {
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                for (Assignment assignment: course.getAssignments()) {
                    if (assignment.getAssignmentID() == assignmentID)
                        return assignment;
                }
            }
        }
        return null;
    }

    public static CourseHour findCourseHourByID(long courseHourID) {
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                for (CourseHour courseHour: course.getCourseHours()) {
                    if (courseHour.getCourseHourID() == courseHourID)
                        return courseHour;
                }
            }
        }
        return null;
    }

    public static Note findNoteByID(long noteID) {
        for (Semester semester: semesters) {
            for (Course course: semester.getCourses()) {
                for (CourseHour courseHour: course.getCourseHours()) {
                    for (Note note: courseHour.getNotes()) {
                        if (note.getNoteID() == noteID)
                            return note;
                    }
                }
            }
        }
        return null;
    }

    public static GradingSystem findGradingSystemByID(long gradingSystemID) {
        for (GradingSystem gradingSystem: gradingSystems) {
            if (gradingSystem.getGradingSystemID() == gradingSystemID)
                return gradingSystem;
        }
        return null;
    }

    public static void setAttendance(Context context, CourseHour courseHour, int attendance) {
        courseHour.setAttendance(attendance);
        CourseDiaryDB.getDBInstance(context).courseHourDAO().update(courseHour);
    }

    public static boolean getCourseHoursEmpty() {
        for (Semester semester: semesters) {
            if (!semester.getCourseHoursEmpty())
                return false;
        }
        return true;
    }

    public static boolean getCoursesEmpty() {
        for (Semester semester: semesters) {
            if (semester.getCourses().size() > 0)
                return false;
        }
        return true;
    }

    public static void integrateWithDB(Context context) { // Get latest data from DB
        semesters = CourseDiaryDB.getDBInstance(context).semesterDAO().getAll();
        gradingSystems = CourseDiaryDB.getDBInstance(context).gradingSystemDAO().getAll();
    }
}