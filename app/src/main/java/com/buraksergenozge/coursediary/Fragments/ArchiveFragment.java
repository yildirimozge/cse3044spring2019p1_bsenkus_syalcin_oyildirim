package com.buraksergenozge.coursediary.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class ArchiveFragment extends BaseFragment {
    private RecyclerView semesterRecyclerView;
    private TextView emptyArchiveTV;
    public static final String tag = "archiveFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_archive;
    }

    private void setVisibilities(boolean isSemesterListEmpty) {
        if(semesterRecyclerView != null && emptyArchiveTV != null) {
            if (isSemesterListEmpty) {
                semesterRecyclerView.setVisibility(View.GONE);
                emptyArchiveTV.setVisibility(View.VISIBLE);
            } else if (emptyArchiveTV.getVisibility() == View.VISIBLE) {
                emptyArchiveTV.setVisibility(View.GONE);
                semesterRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initializeViews() {
        semesterRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.semester_recycler_view);
        semesterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(semesterRecyclerView);
        emptyArchiveTV = getView().findViewById(R.id.emptyArchive_TV);
    }

    @Override
    public void open(AppContent appContent) {
        if (appContent instanceof Semester) {
            childFragment = new SemesterFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, SemesterFragment.tag);
        }
        else {
            super.open(appContent);
        }
    }

    @Override
    public void updateView() {
        boolean isEmpty = updateRecyclerView(semesterRecyclerView, User.getSemesters());
        setVisibilities(isEmpty);
    }
}