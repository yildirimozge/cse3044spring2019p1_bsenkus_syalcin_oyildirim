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
        semesters.add(semester);
        CourseDiaryDB.getDBInstance(context).semesterDAO().addSemester(semester);
    }

    public static void deleteSemester(Context context, Semester semester) {
        semesters.remove(semester);
        CourseDiaryDB.getDBInstance(context).semesterDAO().deleteSemester(semester);
    }

    public static List<Semester> integrateWithDB(Context context) { // Get latest data from DB
        semesters = CourseDiaryDB.getDBInstance(context).semesterDAO().getAll();
        return semesters;
    }
}