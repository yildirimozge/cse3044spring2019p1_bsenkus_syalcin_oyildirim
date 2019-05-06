package com.buraksergenozge.coursediary.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.List;

public class Archive extends Fragment {
    private ListView semesterListView;
    private TextView emptyArchiveTextView;
    private List<Semester> semesters;

    private OnFragmentInteractionListener mListener;

    public Archive() { // Required empty public constructor
    }

    public static Archive newInstance() {
        return new Archive();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        semesterListView = getView().findViewById(R.id.semesterListView);
        emptyArchiveTextView = getView().findViewById(R.id.emptyArchiveTextView);
        boolean isEmpty = updateSemesterList();
        setVisibilities(isEmpty); // If semester database is not empty
    }

    public ListView getSemesterListView() {
        return semesterListView;
    }

    private boolean setVisibilities(boolean isEmpty) {
        if(isEmpty) {
            semesterListView.setVisibility(View.GONE);
            emptyArchiveTextView.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            emptyArchiveTextView.setVisibility(View.GONE);
            semesterListView.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public boolean updateSemesterList() {
        semesters = CourseDiaryDB.getDBInstance(getActivity()).semesterDAO().getAll();
        ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), semesters);
        semesterListView.setAdapter(adapter);
        return semesters.isEmpty();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }
}