package com.buraksergenozge.coursediary.Fragments;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Tools.ItemViewHolder;
import com.buraksergenozge.coursediary.R;

import java.util.Collections;
import java.util.Objects;

public class CourseFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView assignmentRecyclerView, courseHourRecyclerView;
    private TextView courseTitle_TV, grade_TV, attendance_TV;
    public static final String tag = "courseFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_course;
    }

    private void setVisibilities(boolean isCourseHourListEmpty, boolean isAssignmentListEmpty) {
        if(assignmentRecyclerView != null && courseHourRecyclerView != null) {
            if (isCourseHourListEmpty)
                courseHourRecyclerView.setVisibility(View.GONE);
            else
                courseHourRecyclerView.setVisibility(View.VISIBLE);
            if (isAssignmentListEmpty)
                assignmentRecyclerView.setVisibility(View.GONE);
            else
                assignmentRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void open(AppContent appContent) {
        if (appContent instanceof Assignment) {
            childFragment = new AssignmentFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, AssignmentFragment.tag);
        }
        else if (appContent instanceof CourseHour) {
            childFragment = new CourseHourFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, CourseHourFragment.tag);
        }
    }

    private void updateBar() {
        attendance_TV.setText(getResources().getString(R.string.progress_holder, ((Course)appContent).getAttendanceStatus()));
        if (((Course)appContent).getGrade() != null)
            grade_TV.setText(((Course)appContent).getGrade().getCode());
        else
            grade_TV.setText("-");
    }

    @Override
    public void initializeViews() {
        courseTitle_TV = Objects.requireNonNull(getView()).findViewById(R.id.courseTitle_TV);
        grade_TV = getView().findViewById(R.id.grade_TV);
        grade_TV.setOnClickListener(this);
        getView().findViewById(R.id.gradeTitle_TV).setOnClickListener(this);
        attendance_TV = getView().findViewById(R.id.attendance_TV);
        LinearLayout courseHourListHeader = getView().findViewById(R.id.courseHourListHeader);
        courseHourListHeader.setOnClickListener(this);
        assignmentRecyclerView = getView().findViewById(R.id.assignment_recyclerview);
        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(assignmentRecyclerView);
        courseHourRecyclerView = getView().findViewById(R.id.course_hour_recyclerview);
        courseHourRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(courseHourRecyclerView);
        LinearLayout assignmentListHeader = getView().findViewById(R.id.assignmentListHeader);
        assignmentListHeader.setOnClickListener(this);
    }

    @Override
    public void updateView() {
        courseTitle_TV.setText(((Course)appContent).getName());
        Collections.sort(((Course)appContent).getCourseHours());
        Collections.sort(((Course)appContent).getAssignments());
        boolean isCourseHourListEmpty = updateRecyclerView(courseHourRecyclerView, ((Course)appContent).getCourseHours());
        boolean isAssignmentListEmpty = updateRecyclerView(assignmentRecyclerView, ((Course)appContent).getAssignments());
        setVisibilities(isCourseHourListEmpty, isAssignmentListEmpty);
        updateBar();
        if (assignmentRecyclerView.getAdapter() != null) {
            if (assignmentRecyclerView.getAdapter().getItemCount() > 4) {
                ViewGroup.LayoutParams params = assignmentRecyclerView.getLayoutParams();
                params.height = 515;
                assignmentRecyclerView.setLayoutParams(params);
                assignmentRecyclerView.requestLayout();
            }
        }
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.assignmentListHeader_TV)).setText(getResources().getString(R.string.assignments, ((Course)appContent).getAssignments().size()));
        ((TextView)getView().findViewById(R.id.courseHourListHeader_TV)).setText(getResources().getString(R.string.course_hours, ((Course)appContent).getCourseHours().size()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.courseHourListHeader:
                if (courseHourRecyclerView.getVisibility() == View.VISIBLE)
                    courseHourRecyclerView.setVisibility(View.GONE);
                else
                    courseHourRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.assignmentListHeader:
                if (assignmentRecyclerView.getVisibility() == View.VISIBLE)
                    assignmentRecyclerView.setVisibility(View.GONE);
                else
                    assignmentRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.listItemCheckBox:
                ItemViewHolder itemViewHolder = (ItemViewHolder)view.getTag();
                CourseHour courseHour = (CourseHour) itemViewHolder.appContent;
                if (((CheckBox)view).isChecked())
                    User.setAttendance(getContext(), courseHour, 1);
                else
                    User.setAttendance(getContext(), courseHour, 0);
                updateView();
                break;
            case R.id.gradeTitle_TV:
            case R.id.grade_TV:
                if (((Course)appContent).getGradingSystem() == null) {
                    Toast.makeText(getContext(), getString(R.string.set_grading_system_warning), Toast.LENGTH_SHORT).show();
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle(R.string.select_grade);
                String[] gradeCodes = new String[((Course)appContent).getGradingSystem().getGradeList().size() + 1];
                for (int i = 0; i < ((Course)appContent).getGradingSystem().getGradeList().size(); i++) {
                    gradeCodes[i] = (((Course)appContent).getGradingSystem().getGradeList().get(i)).getCode();
                }
                gradeCodes[gradeCodes.length - 1] = "-";
                builder.setItems(gradeCodes, (MainScreen)getActivity());
                MainScreen.activeDialog = "gradeDialog";
                builder.show();
                break;
        }
    }
}