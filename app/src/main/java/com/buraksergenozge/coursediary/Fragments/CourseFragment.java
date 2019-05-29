package com.buraksergenozge.coursediary.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

public class CourseFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "courseID";
    public Course course;
    private ListView courseHourListView, assignmentListView;
    private TextView emptyCourseTV;

    public CourseFragment() {
    }

    public static CourseFragment newInstance(long courseID) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, courseID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.fragment_course;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long courseID = getArguments().getLong(ARG_PARAM1);
            course = User.findCourseByID(courseID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView semesterTitleTV = getView().findViewById(R.id.courseTitle_TV);
        semesterTitleTV.setText(course.getName());
        courseHourListView = getView().findViewById(R.id.courseHourListView);
        courseHourListView.setOnItemClickListener(this);
        registerForContextMenu(courseHourListView);
        assignmentListView = getView().findViewById(R.id.assignmentListView);
        assignmentListView.setOnItemClickListener(this);
        registerForContextMenu(assignmentListView);

        emptyCourseTV = getView().findViewById(R.id.emptyCourse_TV);
        updateView();
    }

    public void setVisibilities(boolean isCourseHourListEmpty, boolean isAssignmentListEmpty) {
        if(assignmentListView != null && courseHourListView != null && emptyCourseTV != null) {
            if (isCourseHourListEmpty && isAssignmentListEmpty) {
                courseHourListView.setVisibility(View.GONE);
                assignmentListView.setVisibility(View.GONE);
                emptyCourseTV.setVisibility(View.VISIBLE);
            } else if (emptyCourseTV.getVisibility() == View.VISIBLE) {
                courseHourListView.setVisibility(View.VISIBLE);
                assignmentListView.setVisibility(View.VISIBLE);
                emptyCourseTV.setVisibility(View.GONE);
            }
        }
    }

    public boolean updateCourseHourList() {
        //course.setCourseHours(CourseDiaryDB.getDBInstance(getActivity()).courseDAO().getAllCourseHoursOfCourse(course));
        ListAdapter<CourseHour> adapter = new ListAdapter<>(getActivity(), course.getCourseHours());
        courseHourListView.setAdapter(adapter);
        ((BaseAdapter)courseHourListView.getAdapter()).notifyDataSetChanged();
        return course.getCourseHours().isEmpty();
    }

    public boolean updateAssignmentList() {
        //course.setAssignments(CourseDiaryDB.getDBInstance(getActivity()).courseDAO().getAllAssignmentsOfCourse(course));
        ListAdapter<Assignment> adapter = new ListAdapter<>(getActivity(), course.getAssignments());
        assignmentListView.setAdapter(adapter);
        ((BaseAdapter)assignmentListView.getAdapter()).notifyDataSetChanged();
        return course.getAssignments().isEmpty();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == courseHourListView) {
            long courseHourID = ((CourseHour)adapterView.getItemAtPosition(i)).getCourseHourID();
            FragmentManager fragManager;
            try {
                fragManager = getActivity().getSupportFragmentManager();
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                return;
            }/*
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            SemesterFragment semesterFragment = SemesterFragment.newInstance(semesterID);
            fragTransaction.replace(R.id.mainArchiveLayout, semesterFragment,"semesterFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();*/
        }
        else if (adapterView == assignmentListView) {
            long assignmentID = ((Assignment)adapterView.getItemAtPosition(i)).getAssignmentID();
            FragmentManager fragManager;
            try {
                fragManager = getActivity().getSupportFragmentManager();
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                return;
            }/*
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            SemesterFragment semesterFragment = SemesterFragment.newInstance(semesterID);
            fragTransaction.replace(R.id.mainArchiveLayout, semesterFragment,"semesterFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();*/
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object object = contextMenuSelectedListView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.floating_delete:
                if (contextMenuSelectedListView == assignmentListView) {
                    course.deleteAssignment(getContext(), (Assignment) object);
                    ((MainScreen)getActivity()).onAppContentOperation("courseFragment", getString(R.string.assignment_deleted));
                }
                else if (contextMenuSelectedListView == courseHourListView) {
//                    course.deleteCourseHour(getContext(), courseHour);
//                    ((MainScreen)getActivity()).onAppContentOperation(getString(R.string.semester_deleted));
                }
                return true;
            case R.id.floating_info:
                if (contextMenuSelectedListView == assignmentListView) {
                    Toast.makeText(getContext(), object.toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
                }
                else if (contextMenuSelectedListView == courseHourListView) {
                    Toast.makeText(getContext(), object.toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void updateView() {
        boolean isCourseHourListEmpty = updateCourseHourList();
        boolean isAssignmentListEmpty = updateAssignmentList();
        setVisibilities(isCourseHourListEmpty, isAssignmentListEmpty);
    }
}
