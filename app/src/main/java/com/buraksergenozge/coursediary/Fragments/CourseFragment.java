package com.buraksergenozge.coursediary.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.Collections;

public class CourseFragment extends ListFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "courseID";
    public Course course;
    private LinearLayout courseHourListHeader, assignmentListHeader;
    private ListView courseHourListView, assignmentListView;
    private TextView emptyCourseHourList_TV, emptyAssignmentList_TV;

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
        courseHourListHeader = getView().findViewById(R.id.courseHourListHeader);
        courseHourListHeader.setOnClickListener(this);
        courseHourListView = getView().findViewById(R.id.courseHourListView);
        courseHourListView.setOnItemClickListener(this);
        registerForContextMenu(courseHourListView);
        assignmentListHeader = getView().findViewById(R.id.assignmentListHeader);
        assignmentListHeader.setOnClickListener(this);
        assignmentListView = getView().findViewById(R.id.assignmentListView);
        assignmentListView.setOnItemClickListener(this);
        registerForContextMenu(assignmentListView);
        emptyCourseHourList_TV = getView().findViewById(R.id.emptyCourseHourList_TV);
        emptyAssignmentList_TV = getView().findViewById(R.id.emptyAssignmentList_TV);
        updateView();
        contextObject = course;
    }

    public void setVisibilities(boolean isCourseHourListEmpty, boolean isAssignmentListEmpty) {
        if(assignmentListView != null && courseHourListView != null && emptyCourseHourList_TV != null && emptyAssignmentList_TV != null) {
            if (isCourseHourListEmpty) {
                courseHourListView.setVisibility(View.GONE);
                emptyCourseHourList_TV.setVisibility(View.VISIBLE);
            }
            else {
                courseHourListView.setVisibility(View.VISIBLE);
                emptyCourseHourList_TV.setVisibility(View.GONE);
            }
            if (isAssignmentListEmpty) {
                assignmentListView.setVisibility(View.GONE);
                emptyAssignmentList_TV.setVisibility(View.VISIBLE);
            }
            else {
                assignmentListView.setVisibility(View.VISIBLE);
                emptyAssignmentList_TV.setVisibility(View.GONE);
            }
        }
    }

    public boolean updateCourseHourList() {
        Collections.sort(course.getCourseHours());
        ListAdapter<CourseHour> adapter = new ListAdapter<>(getActivity(), course.getCourseHours());
        courseHourListView.setAdapter(adapter);
        ((BaseAdapter)courseHourListView.getAdapter()).notifyDataSetChanged();
        return course.getCourseHours().isEmpty();
    }

    public boolean updateAssignmentList() {
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
            }
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            CourseHourFragment courseHourFragment = CourseHourFragment.newInstance(courseHourID);
            fragTransaction.replace(R.id.mainArchiveLayout, courseHourFragment,"courseHourFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
        }
        else if (adapterView == assignmentListView) {
            long assignmentID = ((Assignment)adapterView.getItemAtPosition(i)).getAssignmentID();
            FragmentManager fragManager;
            try {
                fragManager = getActivity().getSupportFragmentManager();
            }catch (NullPointerException ex) {
                ex.printStackTrace();
                return;
            }
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            AssignmentFragment assignmentFragment = AssignmentFragment.newInstance(assignmentID);
            fragTransaction.replace(R.id.mainArchiveLayout, assignmentFragment,"assignmentFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Object object = contextMenuSelectedListView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.floating_delete:
                if (contextMenuSelectedListView == assignmentListView) {
                    new AlertDialog.Builder(getContext()).setMessage(getString(R.string.confirm_delete)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    course.deleteAssignment(getContext(), (Assignment) object);
                                    ((MainScreen)getActivity()).onAppContentOperation("courseFragment", getString(R.string.assignment_deleted));
                                }}).setNegativeButton(R.string.no, null).show();
                }
                else if (contextMenuSelectedListView == courseHourListView) {
                    new AlertDialog.Builder(getContext()).setMessage(getString(R.string.confirm_delete)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            course.deleteCourseHour(getContext(), (CourseHour) object);
                            ((MainScreen)getActivity()).onAppContentOperation("courseFragment", getString(R.string.course_hour_deleted));
                        }}).setNegativeButton(R.string.no, null).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courseHourListHeader:
                if (courseHourListView.getVisibility() == View.VISIBLE)
                    courseHourListView.setVisibility(View.GONE);
                else
                    courseHourListView.setVisibility(View.VISIBLE);
                break;
            case R.id.assignmentListHeader:
                if (assignmentListView.getVisibility() == View.VISIBLE)
                    assignmentListView.setVisibility(View.GONE);
                else
                    assignmentListView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
