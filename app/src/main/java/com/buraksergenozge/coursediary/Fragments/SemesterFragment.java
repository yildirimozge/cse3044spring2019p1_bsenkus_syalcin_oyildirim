package com.buraksergenozge.coursediary.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class SemesterFragment extends BaseFragment {
    private RecyclerView courseRecyclerView;
    private TextView daysToEnd, gpa_TV, semesterTitleTV, emptySemesterTV;
    public static final String tag = "semesterFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_semester;
    }

    @Override
    public void open(AppContent appContent) {
        if (appContent instanceof Course) {
            childFragment = new CourseFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, CourseFragment.tag);
        }
        else {
            super.open(appContent);
        }
    }

    private void setVisibilities(boolean isCourseListEmpty) {
        if(courseRecyclerView != null && emptySemesterTV != null) {
            if(isCourseListEmpty) {
                courseRecyclerView.setVisibility(View.GONE);
                emptySemesterTV.setVisibility(View.VISIBLE);
            }
            else if(emptySemesterTV.getVisibility() == View.VISIBLE) {
                emptySemesterTV.setVisibility(View.GONE);
                courseRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initializeViews() {
        semesterTitleTV = Objects.requireNonNull(getView()).findViewById(R.id.semesterTitle_TV);
        daysToEnd = getView().findViewById(R.id.daysToEnd);
        gpa_TV = getView().findViewById(R.id.gpa_TV);
        courseRecyclerView = getView().findViewById(R.id.course_recycler_view);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(courseRecyclerView);
        emptySemesterTV = getView().findViewById(R.id.emptySemester_TV);
    }

    @Override
    public void updateView() {
        semesterTitleTV.setText(((Semester)appContent).getName());
        daysToEnd.setText(getResources().getString(R.string.int_holder, ((Semester) appContent).getNumberOfDaysRemaining()));
        gpa_TV.setText(getResources().getString(R.string.float_holder, ((Semester) appContent).getGpa()));
        boolean isEmpty = updateRecyclerView(courseRecyclerView, ((Semester)appContent).getCourses());
        setVisibilities(isEmpty);
    }
}