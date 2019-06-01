package com.buraksergenozge.coursediary.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

public class AssignmentFragment extends ListFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "assignmentID";
    public Assignment assignment;/*
    private LinearLayout courseHourListHeader, assignmentListHeader;
    private ListView courseHourListView, assignmentListView;
    private TextView emptyCourseHourList_TV, emptyAssignmentList_TV;*/

    public AssignmentFragment() {
    }

    public static AssignmentFragment newInstance(long assignmentID) {
        AssignmentFragment fragment = new AssignmentFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, assignmentID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        layoutID = R.layout.fragment_assignment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long assignmentID = getArguments().getLong(ARG_PARAM1);
            assignment = User.findAssignmentByID(assignmentID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*TextView courseHourTitle_TV = getView().findViewById(R.id.courseTitle_TV);
        courseHourTitle_TV.setText(course.getName());
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
        contextObject = course;*/
    }/*

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
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        /*if (adapterView == courseHourListView) {
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
            SemesterFragment semesterFragment = SemesterFragment.newInstance(semesterID);
            fragTransaction.replace(R.id.mainArchiveLayout, semesterFragment,"semesterFragment");
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
        }*/
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
        }*/return super.onContextItemSelected(item);
    }

    @Override
    public void updateView() {
        /*boolean isCourseHourListEmpty = updateCourseHourList();
        boolean isAssignmentListEmpty = updateAssignmentList();
        setVisibilities(isCourseHourListEmpty, isAssignmentListEmpty);*/
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
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
        }*/
    }
}
