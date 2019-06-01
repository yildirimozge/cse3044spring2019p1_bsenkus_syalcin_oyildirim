package com.buraksergenozge.coursediary.Fragments;

import android.support.v4.app.Fragment;

import com.buraksergenozge.coursediary.Activities.MainScreen;

public abstract class BaseFragment extends Fragment {
    public static Object contextObject; // When creating an assignment etc. this object helps to determine where add button clicked to automatically add it to that object

    @Override
    public void onResume() {
        super.onResume();
        MainScreen.activeFragmentTag = getTag();
    }
}
