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

    public static void addSemester(Context context, Semester semester) {
        semesters.add(semester);
        CourseDiaryDB.getDBInstance(context).semesterDAO().addSemester(semester);
    }

    public static void deleteSemester(Context context, Semester semester) {
        semesters.remove(semester);
        CourseDiaryDB.getDBInstance(context).semesterDAO().addSemester(semester);
    }
}