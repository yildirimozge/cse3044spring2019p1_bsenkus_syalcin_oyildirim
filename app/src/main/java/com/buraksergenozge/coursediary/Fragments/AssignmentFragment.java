package com.buraksergenozge.coursediary.Fragments;

import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.StringManager;

import java.util.Calendar;
import java.util.Objects;

public class AssignmentFragment extends BaseFragment {
    private TextView title_TV, course_TV, timeRemaining_TV, deadline_TV;
    public static final String tag = "assignmentFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_assignment;
    }

    @Override
    public void initializeViews() {
        title_TV = Objects.requireNonNull(getView()).findViewById(R.id.assignmentTitle_TV);
        timeRemaining_TV = getView().findViewById(R.id.deadline_TV);
        course_TV = getView().findViewById(R.id.assignment_course_TV);
        deadline_TV = getView().findViewById(R.id.assignment_deadline_TV);
    }

    @Override
    public void updateView() {
        title_TV.setText(((Assignment)appContent).getTitle());
        timeRemaining_TV.setText(StringManager.getTimeRepresentation(AppContent.getTimeDifferenceInMillis(Calendar.getInstance(), ((Assignment)appContent).getDeadline()), getResources()));
        course_TV.setText(((Assignment)appContent).getCourse().getName());
        deadline_TV.setText(StringManager.getDateString(((Assignment)appContent).getDeadline(),"EEE, d MMM yyyy HH:mm"));
    }
}