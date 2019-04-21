package com.buraksergenozge.coursediary.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

    public static Archive newInstance(String param1, String param2) {
        Archive fragment = new Archive();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
       // semesters = ProjectDatabase.getDBInstance(getActivity()).semesterDAO().getAll();
        setVisibilities();
    }

    private void setVisibilities() {
        if(semesters == null || semesters.isEmpty()) {
            semesterListView.setVisibility(View.GONE);
            emptyArchiveTextView.setVisibility(View.VISIBLE);
        }
        else {
            emptyArchiveTextView.setVisibility(View.GONE);
            semesterListView.setVisibility(View.VISIBLE);
            //ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), ProjectDatabase.getDBInstance(getActivity()).semesterDAO().getAll());
            //semesterListView.setAdapter(adapter);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}