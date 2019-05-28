package com.buraksergenozge.coursediary.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.CourseDiaryDB;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.ListAdapter;
import com.buraksergenozge.coursediary.R;

public class ArchiveFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView semesterListView;
    private TextView emptyArchiveTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        semesterListView = getView().findViewById(R.id.semesterListView);
        registerForContextMenu(semesterListView);
        semesterListView.setOnItemClickListener(this);
        emptyArchiveTV = getView().findViewById(R.id.emptyArchive_TV);
        boolean isEmpty = updateSemesterList();
        setVisibilities(isEmpty); // If semester database is not empty
    }

    public void setVisibilities(boolean isSemesterListEmpty) {
        if(semesterListView != null && emptyArchiveTV != null) {
            if (isSemesterListEmpty) {
                semesterListView.setVisibility(View.GONE);
                emptyArchiveTV.setVisibility(View.VISIBLE);
            } else if (emptyArchiveTV.getVisibility() == View.VISIBLE) {
                emptyArchiveTV.setVisibility(View.GONE);
                semesterListView.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean updateSemesterList() {
        User.setSemesters(CourseDiaryDB.getDBInstance(getActivity()).semesterDAO().getAll());
        ListAdapter<Semester> adapter = new ListAdapter<>(getActivity(), User.getSemesters());
        semesterListView.setAdapter(adapter);
        ((BaseAdapter)semesterListView.getAdapter()).notifyDataSetChanged();
        return User.getSemesters().isEmpty();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == semesterListView) {
            long semesterID = ((Semester)adapterView.getItemAtPosition(i)).getSemesterID();
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
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            getActivity().getMenuInflater().inflate(R.menu.menu_floating, menu);
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Semester semester = (Semester)semesterListView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.floating_delete:
                User.deleteSemester(getContext(), semester);
                ((MainScreen)getActivity()).onSemesterOperation(getString(R.string.semester_deleted));
                return true;
            case R.id.floating_info:
                Toast.makeText(getContext(), semester.toString() + " BİLGİSİ GÖSTERİLECEK", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}