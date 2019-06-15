package com.buraksergenozge.coursediary.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class GradingSystemListFragment extends BaseFragment {
    private TextView emptyGradingSystem_TV;
    private RecyclerView gradingSystemRecyclerView;
    public static final String tag = "gradingSystemListFragment";

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_grading_system_list;
    }

    @Override
    protected void initializeViews() {
        gradingSystemRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.grading_system_recycler_view);
        gradingSystemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(gradingSystemRecyclerView);
        emptyGradingSystem_TV = getView().findViewById(R.id.empty_grading_system_TV);
    }

    @Override
    public void open(AppContent appContent) {
    }

    private void setVisibilities(boolean isGradingSystemListEmpty) {
        if(gradingSystemRecyclerView != null && emptyGradingSystem_TV != null) {
            if (isGradingSystemListEmpty) {
                gradingSystemRecyclerView.setVisibility(View.GONE);
                emptyGradingSystem_TV.setVisibility(View.VISIBLE);
            } else if (emptyGradingSystem_TV.getVisibility() == View.VISIBLE) {
                emptyGradingSystem_TV.setVisibility(View.GONE);
                gradingSystemRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updateView() {
        boolean isEmpty = updateRecyclerView(gradingSystemRecyclerView, User.getGradingSystems());
        setVisibilities(isEmpty);
    }
}
