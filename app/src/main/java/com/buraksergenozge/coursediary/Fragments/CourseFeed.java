package com.buraksergenozge.coursediary.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

import java.util.List;
import java.util.Objects;

public class CourseFeed extends BaseFragment implements View.OnClickListener {
    private RecyclerView assignmentRecyclerView, courseHourRecyclerView;
    public static final String tag = "courseFeedFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_course_feed;
    }

    private void setVisibilities(boolean isCourseHourListEmpty, boolean isAssignmentListEmpty) {
        if(assignmentRecyclerView != null && courseHourRecyclerView != null) {
            if (isCourseHourListEmpty)
                courseHourRecyclerView.setVisibility(View.GONE);
            else
                courseHourRecyclerView.setVisibility(View.VISIBLE);
            if (isAssignmentListEmpty)
                assignmentRecyclerView.setVisibility(View.GONE);
            else
                assignmentRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initializeViews() {
        LinearLayout assignmentListHeader = Objects.requireNonNull(getView()).findViewById(R.id.coursefeed_assignmentListHeader);
        assignmentListHeader.setOnClickListener(this);
        LinearLayout courseHourListHeader = getView().findViewById(R.id.coursefeed_courseHourListHeader);
        courseHourListHeader.setOnClickListener(this);
        assignmentRecyclerView = getView().findViewById(R.id.coursefeed_assignment_recyclerview);
        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(assignmentRecyclerView);
        courseHourRecyclerView = getView().findViewById(R.id.coursefeed_coursehour_recyclerview);
        courseHourRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(courseHourRecyclerView);
    }

    @Override
    public void updateView() {
        List<Assignment> assignmentList = User.getActiveAssignments();
        List<CourseHour> courseHourList = User.getUpcomingCourseHours();
        boolean isCourseHourListEmpty = updateRecyclerView(courseHourRecyclerView, courseHourList);
        boolean isAssignmentListEmpty = updateRecyclerView(assignmentRecyclerView, assignmentList);
        setVisibilities(isCourseHourListEmpty, isAssignmentListEmpty);
        if (assignmentRecyclerView.getAdapter() != null) {
            if (assignmentRecyclerView.getAdapter().getItemCount() > 4) {
                ViewGroup.LayoutParams params = assignmentRecyclerView.getLayoutParams();
                params.height = 515;
                assignmentRecyclerView.setLayoutParams(params);
                assignmentRecyclerView.requestLayout();
            }
        }
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.coursefeed_assignmentListHeader_TV)).setText(getResources().getString(R.string.assignments, assignmentList.size()));
        ((TextView)getView().findViewById(R.id.coursefeed_courseHourListHeader_TV)).setText(getResources().getString(R.string.course_hours, courseHourList.size()));
    }

    @Override
    public void open(AppContent appContent) {
        if (appContent instanceof Assignment) {
            childFragment = new AssignmentFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, AssignmentFragment.tag);
        }
        else if (appContent instanceof CourseHour) {
            childFragment = new CourseHourFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, CourseHourFragment.tag);
        }
        else {
            super.open(appContent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coursefeed_assignmentListHeader:
                if (assignmentRecyclerView.getVisibility() == View.VISIBLE)
                    assignmentRecyclerView.setVisibility(View.GONE);
                else
                    assignmentRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.coursefeed_courseHourListHeader:
                if (courseHourRecyclerView.getVisibility() == View.VISIBLE)
                    courseHourRecyclerView.setVisibility(View.GONE);
                else
                    courseHourRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }
}