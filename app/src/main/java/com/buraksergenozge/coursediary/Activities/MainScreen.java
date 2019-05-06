package com.buraksergenozge.coursediary.Activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Fragments.Archive;
import com.buraksergenozge.coursediary.Fragments.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.SemesterCreationDialog;
import com.buraksergenozge.coursediary.PagerAdapter;
import com.buraksergenozge.coursediary.R;

public class MainScreen extends Screen implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener,
        DialogInterface.OnClickListener, Archive.OnFragmentInteractionListener, CourseFeed.OnFragmentInteractionListener,
        SemesterCreationDialog.OnFragmentInteractionListener, AssignmentCreationDialog.OnFragmentInteractionListener {
    public View mainScreenLayout;
    private ViewPager viewPager;
    private String activeDialog;
    private Fragment feedFragment, archiveFragment;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hasMenu = true;
        menuID = R.menu.menu_main;

        mainScreenLayout = findViewById(R.id.mainScreenLayout);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.main_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
        feedFragment = pagerAdapter.getItem(0);
        archiveFragment = pagerAdapter.getItem(1);
    }

    public Fragment getFeedFragment() {
        return feedFragment;
    }

    public Fragment getArchiveFragment() {
        return archiveFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) { // View interaction
        switch (view.getId()) {
            case R.id.addButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.add_new);
                String[] addOptions = {getString(R.string.semester), getString(R.string.course), getString(R.string.assignment),
                        getString(R.string.note), getString(R.string.photo), getString(R.string.audio)};
                builder.setItems(addOptions, this);
                activeDialog = "creationDialog";
                builder.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) { // Dialog interaction
        if(activeDialog.equals("creationDialog")) {
            switch (i) {
                case 0:
                    Semester.openCreationDialog(this, Semester.getCreationDialog());
                    break;
                case 1:
                    Course.openCreationDialog(this, Course.getCreationDialog());
                    break;
                case 2:
                    Assignment.openCreationDialog(this, Assignment.getCreationDialog());
                    break;
                case 3:
                   // Note.openCreationDialog(this);
                    break;
                case 4:
                    //Photo.openCreationDialog(this);
                    break;
                case 5:
                   // Audio.openCreationDialog(this);
                    break;
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onSemesterAdded() {
        ((Archive)archiveFragment).updateSemesterList();
    }

    @Override
    public void onAssignmentAdded() {

    }
}