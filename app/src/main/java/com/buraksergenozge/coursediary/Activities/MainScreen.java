package com.buraksergenozge.coursediary.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Fragments.Archive;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.ListFragment;
import com.buraksergenozge.coursediary.PagerAdapter;
import com.buraksergenozge.coursediary.R;

public class MainScreen extends Screen implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener,
        DialogInterface.OnClickListener, CreationDialog.OnFragmentInteractionListener, CourseFeed.OnFragmentInteractionListener {
    public View mainScreenLayout;
    private ViewPager viewPager;
    private String activeDialog;
    public static Archive mainArchiveFragment;
    public static CourseFeed courseFeed;
    public static String activeFragmentTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        integrateData(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hasMenu = true;
        menuID = R.menu.menu_main;

        mainScreenLayout = findViewById(R.id.mainScreenLayout);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.main_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
        courseFeed = (CourseFeed) pagerAdapter.getItem(0);
        mainArchiveFragment = (Archive) pagerAdapter.getItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_edit:
                Fragment activeFragment = getSupportFragmentManager().findFragmentByTag(MainScreen.activeFragmentTag);
                if (activeFragment != null && activeFragment.isVisible()) {
                    if (BaseFragment.contextObject != null)
                        Toast.makeText(this, "Fragment: " + activeFragment.toString() + "\nObject: " + BaseFragment.contextObject.toString(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Fragment: " + activeFragment.toString() + "\nObject: null", Toast.LENGTH_SHORT).show();
                }
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
                builder.setItems(getResources().getStringArray(R.array.app_contents), this);
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
                    activeDialog = "";
                    break;
                case 1:
                    Course.openCreationDialog(this, Course.getCreationDialog());
                    activeDialog = "";
                    break;
                case 2:
                    Assignment.openCreationDialog(this, Assignment.getCreationDialog());
                    activeDialog = "";
                    break;
                case 3:
                   // Note.openCreationDialog(this);
                    activeDialog = "";
                    break;
                case 4:
                    //Photo.openCreationDialog(this);
                    activeDialog = "";
                    break;
                case 5:
                   // Audio.openCreationDialog(this);
                    activeDialog = "";
                    break;
            }
        }
    }

    private void integrateData(Context context) {
        User.integrateWithDB(context);
        for (Semester semester: User.getSemesters()) {
            semester.integrateWithDB(context);
            for (Course course : semester.getCourses()) {
                course.integrateWithDB(context);
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
    public void onAppContentOperation(String listFragmentTag, String message) {
        ListFragment fragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(listFragmentTag);
        if (fragment != null && activeFragmentTag.equals(listFragmentTag))
            fragment.updateView();
        showSnackbarMessage(getWindow().getDecorView(), message);
    }

    public static void showSnackbarMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }
}
