package com.buraksergenozge.coursediary.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class MainCourseFeed extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_coursefeed, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!MainScreen.mainCourseFeedReady) {
            FragmentManager fragManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragManager.beginTransaction();
            fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            CourseFeed courseFeed = new CourseFeed();
            fragTransaction.replace(R.id.mainCourseFeedLayout, courseFeed, CourseFeed.tag);
            fragTransaction.commit();
            MainScreen.mainCourseFeedReady = true;
        }
    }
}