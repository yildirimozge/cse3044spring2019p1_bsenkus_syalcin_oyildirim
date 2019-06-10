package com.buraksergenozge.coursediary.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Tools.RecyclerAdapter;

import java.util.List;
import java.util.Objects;

public abstract class BaseFragment extends Fragment {
    public AppContent appContent;
    static AppContent transferAppContent;
    BaseFragment parentFragment;
    BaseFragment childFragment;

    protected abstract int getLayoutID();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContent = transferAppContent;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainScreen.activeAppContent = appContent;
        updateView();
        MainScreen.updateActiveFragmentTag(getTag());
        ((MainScreen) Objects.requireNonNull(getContext())).supportInvalidateOptionsMenu();
    }

    protected abstract void initializeViews();

    public abstract void updateView();

    void openFragment(BaseFragment fragment, String fragmentTag) {
        FragmentManager fragManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragTransaction.replace(MainScreen.activeTabID, fragment, fragmentTag);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }

    public abstract void open(AppContent appContent);

    boolean updateRecyclerView(RecyclerView recyclerView, List<?> list) {
        RecyclerAdapter<?> adapter = new RecyclerAdapter<>(this, list, (MainScreen) getActivity());
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        return list.isEmpty();
    }

    public boolean onBackPressed() {
        if (parentFragment != null)
            parentFragment.childFragment = null;
        parentFragment = null;
        return true;
    }
}