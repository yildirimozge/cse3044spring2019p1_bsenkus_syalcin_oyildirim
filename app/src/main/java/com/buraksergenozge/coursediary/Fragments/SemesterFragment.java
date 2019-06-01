package com.buraksergenozge.coursediary.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

public class SemesterFragment extends ListFragment implements AdapterView.OnItemClickListener {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.fragment_semester;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long semesterID = getArguments().getLong(ARG_PARAM1);
            semester = User.findSemesterByID(semesterID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView semesterTitleTV = getView().findViewById(R.id.semesterTitle_TV);
        semesterTitleTV.setText(semester.getName());
        ((TextView)getView().findViewById(R.id.daysToEnd)).setText(semester.getNumberOfDaysRemaining() + "");
        ((TextView)getView().findViewById(R.id.gpa_TV)).setText(semester.getGpa() + "");
        courseListView = getView().findViewById(R.id.courseListView);
        courseListView.setOnItemClickListener(this);
        registerForContextMenu(courseListView);
        emptySemesterTV = getView().findViewById(R.id.emptySemester_TV);
        updateView();
        contextObject = semester;
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
        ListAdapter<Course> adapter = new ListAdapter<>(getActivity(), semester.getCourses());
        courseListView.setAdapter(adapter);
        ((BaseAdapter)courseListView.getAdapter()).notifyDataSetChanged();
        return semester.getCourses().isEmpty();
    }

    @Override
    public void updateView() {
        boolean isEmpty = updateCourseList();
        setVisibilities(isEmpty);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Course course = (Course)courseListView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.floating_delete:
                new AlertDialog.Builder(getContext()).setMessage(getString(R.string.confirm_delete)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        semester.deleteCourse(getContext(), course);
                        ((MainScreen)getActivity()).onAppContentOperation("semesterFragment", getString(R.string.course_deleted));
                    }}).setNegativeButton(R.string.no, null).show();
                return true;
            case R.id.floating_info:
                Toast.makeText(getContext(), course.toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
