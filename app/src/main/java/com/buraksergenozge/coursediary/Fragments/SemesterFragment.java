package com.buraksergenozge.coursediary.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

public class SemesterFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "semesterID";
    public Semester semester;

    private ListView courseListView;
    private TextView emptySemesterTV;

    public SemesterFragment() {
    }

    public static SemesterFragment newInstance(long semesterID) {
        SemesterFragment fragment = new SemesterFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, semesterID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long semesterID = getArguments().getLong(ARG_PARAM1);
            semester = CourseDiaryDB.getDBInstance(getContext()).semesterDAO().find(semesterID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_semester, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView semesterTitleTV = getView().findViewById(R.id.semesterTitle_TV);
        semesterTitleTV.setText("Semester: " + semester.toString());
        courseListView = getView().findViewById(R.id.courseListView);
        courseListView.setOnItemClickListener(this);
        registerForContextMenu(courseListView);
        emptySemesterTV = getView().findViewById(R.id.emptySemester_TV);
        boolean isEmpty = updateCourseList();
        setVisibilities(isEmpty);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == courseListView) {
            long courseID = ((Course)adapterView.getItemAtPosition(i)).getCourseID();
            FragmentManager fragManager;
            try {
                fragManager = getActivity().getSupportFragmentManager();
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                return;
            }
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            CourseFragment courseFragment = CourseFragment.newInstance(courseID);
            fragTransaction.replace(R.id.mainArchiveLayout, courseFragment,"courseFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
        }
    }

    public void setVisibilities(boolean isCourseListEmpty) {
        if(courseListView != null && emptySemesterTV != null) {
            if(isCourseListEmpty) {
                courseListView.setVisibility(View.GONE);
                emptySemesterTV.setVisibility(View.VISIBLE);
            }
            else if(emptySemesterTV.getVisibility() == View.VISIBLE) {
                emptySemesterTV.setVisibility(View.GONE);
                courseListView.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean updateCourseList() {
        semester.setCourses(CourseDiaryDB.getDBInstance(getActivity()).semesterDAO().getAllCoursesOfSemester(semester));
        ListAdapter<Course> adapter = new ListAdapter<>(getActivity(), semester.getCourses());
        courseListView.setAdapter(adapter);
        ((BaseAdapter)courseListView.getAdapter()).notifyDataSetChanged();
        return semester.getCourses().isEmpty();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            getActivity().getMenuInflater().inflate(R.menu.menu_floating, menu);
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Course course = (Course)courseListView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.floating_delete:
                semester.deleteCourse(getContext(), course);
                ((MainScreen)getActivity()).onCourseOperation(getString(R.string.course_deleted));
                return true;
            case R.id.floating_info:
                Toast.makeText(getContext(), course.toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
