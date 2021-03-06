package com.buraksergenozge.coursediary.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Grade;
import com.buraksergenozge.coursediary.Data.GradingSystem;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Fragments.ArchiveFragment;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;
import com.buraksergenozge.coursediary.Fragments.BaseFragment;
import com.buraksergenozge.coursediary.Fragments.CourseFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.SettingsFragment;
import com.buraksergenozge.coursediary.Tools.ItemViewHolder;
import com.buraksergenozge.coursediary.Tools.PagerAdapter;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class MainScreen extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener, DialogInterface.OnClickListener {
    public static String activeDialog;
    private static String activeArchiveFragmentTag = ArchiveFragment.tag;
    private static String activeCourseFeedFragmentTag = CourseFeed.tag;
    public static int activeTabID = R.id.mainCourseFeedLayout;
    private static int startControl = 2;
    public static AppContent activeAppContent;
    public static AppContent contextMenuAppContent;
    public static final int CAMERA_REQUEST = 1888;
    private static final int REQUEST_PERMISSION_CODE = 100;
    public static boolean mainCourseFeedReady = false;
    public static boolean mainArchiveReady = false;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        integrateData(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.main_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_record:
                if (!Audio.isRecorderActive) {
                    if (Audio.record(this))
                        item.setIcon(R.drawable.ic_mic_red_24dp);
                }
                else {
                    Audio.mediaRecorder.stop();
                    item.setIcon(R.drawable.ic_mic_light_24dp);
                    Audio.isRecorderActive = false;
                    Log.i("burak", "BİTTİ");
                    if (!(activeAppContent instanceof CourseHour || activeAppContent instanceof Note)) {
                        AppContent.openCreationDialog(this, Audio.getCreationDialog(CreationDialog.CREATE_MODE));
                    }
                    else {
                        Audio audio;
                        if (activeAppContent instanceof CourseHour)
                            audio = new Audio((CourseHour) activeAppContent, Audio.saveAudioPath);
                        else
                            audio = new Audio(((Note) activeAppContent).getCourseHour(), Audio.saveAudioPath);
                        audio.create(this);
                    }
                }
                return true;
            case R.id.action_grading_systems:
                getVisibleFragment().open(null);
                return true;
            case R.id.action_settings:
                DialogFragment fragment = new SettingsFragment();
                fragment.show(getSupportFragmentManager(), SettingsFragment.tag);
                return true;
            case R.id.action_edit:
                if (activeAppContent != null) {
                    activeAppContent.edit(this);
                    activeDialog = "";
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        getVisibleFragment().onBackPressed();
        super.onBackPressed();
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
            case R.id.listItemCheckBox:
                ItemViewHolder itemViewHolder = (ItemViewHolder)view.getTag();
                CourseHour courseHour = (CourseHour) itemViewHolder.appContent;
                if (((CheckBox)view).isChecked())
                    User.setAttendance(this, courseHour, 1);
                else
                    User.setAttendance(this, courseHour, 0);
                if (activeArchiveFragmentTag.equals(CourseFragment.tag))
                    ((BaseFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(CourseFragment.tag))).updateView();
                ((BaseFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag(activeCourseFeedFragmentTag))).updateView();
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
                    AppContent.openCreationDialog(this, Semester.getCreationDialog(CreationDialog.CREATE_MODE));
                    activeDialog = "";
                    break;
                case 1:
                    AppContent.openCreationDialog(this, Course.getCreationDialog(CreationDialog.CREATE_MODE));
                    activeDialog = "";
                    break;
                case 2:
                    AppContent.openCreationDialog(this, Assignment.getCreationDialog(CreationDialog.CREATE_MODE));
                    activeDialog = "";
                    break;
                case 3:
                    AppContent.openCreationDialog(this, Note.getCreationDialog(CreationDialog.CREATE_MODE));
                    activeDialog = "";
                    break;
                case 4:
                    Photo.takePhoto(this);
                    activeDialog = "";
                    break;
                case 5:
                    AppContent.openCreationDialog(this, GradingSystem.getCreationDialog(CreationDialog.CREATE_MODE));
                    activeDialog = "";
                    break;
            }
        }
        else if (activeDialog.equals("gradeDialog")) {
            if (i == ((Course)activeAppContent).getGradingSystem().getGradeList().size())
                ((Course)activeAppContent).setGrade(null);
            else
                ((Course)activeAppContent).setGrade(((Course)activeAppContent).getGradingSystem().getGradeList().get(i));
            activeAppContent.updateOperation(this);
            updateViewsOfAppContent(activeAppContent);
            activeDialog = "";
        }
    }

    private static void integrateData(Context context) {
        User.integrateWithDB(context);
        for (GradingSystem gradingSystem : User.getGradingSystems()) {
            gradingSystem.integrateWithDB(context);
            for (Grade grade: gradingSystem.getGradeList())
                grade.setGradingSystem(gradingSystem);
        }
        for (Semester semester: User.getSemesters()) {
            semester.integrateWithDB(context);
            for (Course course : semester.getCourses()) {
                course.setSemester(semester);
                course.integrateWithDB(context);
                for (CourseHour courseHour: course.getCourseHours()) {
                    courseHour.setCourse(course);
                    courseHour.integrateWithDB(context);
                    for (Note note: courseHour.getNotes())
                        note.setCourseHour(courseHour);
                    for (Photo photo: courseHour.getPhotos())
                        photo.setCourseHour(courseHour);
                    for (Audio audio: courseHour.getAudios())
                        audio.setCourseHour(courseHour);
                }
                for (Assignment assignment: course.getAssignments())
                    assignment.setCourse(course);
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0)
            activeTabID = R.id.mainCourseFeedLayout;
        else if (tab.getPosition() == 1)
            activeTabID = R.id.mainArchiveLayout;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public static void updateActiveFragmentTag(String fragmentTag) {
        if (startControl > 0) { // This part is just for avoiding an error
            startControl--;
            return;
        }
        if (activeTabID == R.id.mainCourseFeedLayout)
            activeCourseFeedFragmentTag = fragmentTag;
        else if (activeTabID == R.id.mainArchiveLayout)
            activeArchiveFragmentTag = fragmentTag;
    }

    public void updateViewsOfAppContent(AppContent appContent) {
        String[] fragmentTags = appContent.getRelatedFragmentTags();
        for (String fragmentTag: fragmentTags) {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (fragment != null && (MainScreen.activeArchiveFragmentTag.equals(fragmentTag) || MainScreen.activeCourseFeedFragmentTag.equals(fragmentTag)))
                fragment.updateView();
        }
    }

    public static void showSnackbarMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }

    public BaseFragment getVisibleFragment() {
        if (activeTabID == R.id.mainCourseFeedLayout)
                return (BaseFragment) getSupportFragmentManager().findFragmentByTag(activeCourseFeedFragmentTag);
        else
            return (BaseFragment) getSupportFragmentManager().findFragmentByTag(activeArchiveFragmentTag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (!(activeAppContent instanceof CourseHour || activeAppContent instanceof Note)) {
                AppContent.openCreationDialog(this, Photo.getCreationDialog(CreationDialog.CREATE_MODE));
            }
            else {
                Photo photo;
                if (activeAppContent instanceof CourseHour)
                    photo = new Photo((CourseHour) activeAppContent, Photo.getSavePhotoPath());
                else
                    photo = new Photo(((Note) activeAppContent).getCourseHour(), Photo.getSavePhotoPath());
                photo.create(this);
            }
        }
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                Toast.makeText(this, "Camera is needed to capture images during course.", Toast.LENGTH_SHORT).show();
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Toast.makeText(this, "It is required to save captured images.", Toast.LENGTH_SHORT).show();
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(this, "It is required to get captured images from storage.", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, MainScreen.REQUEST_PERMISSION_CODE);
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                Toast.makeText(this, "Camera is needed to capture images during course.", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Toast.makeText(this, "It is required to save captured images.", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(this, "It is required to get captured images from storage.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, MainScreen.REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, getString(R.string.permission_grant), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        else
            return (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem recordItem = menu.findItem(R.id.action_record);
        if (MainScreen.activeArchiveFragmentTag == null || MainScreen.activeArchiveFragmentTag.equals(ArchiveFragment.tag))
            editItem.setVisible(false);
        else
            editItem.setVisible(true);
        if (Audio.isRecorderActive)
            recordItem.setIcon(R.drawable.ic_mic_red_24dp);
        else
            recordItem.setIcon(R.drawable.ic_mic_light_24dp);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.floating_delete:
                new AlertDialog.Builder(this).setMessage(getString(R.string.confirm_delete)).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        contextMenuAppContent.remove(MainScreen.this);
                    }}).setNegativeButton(R.string.no, null).show();
                return true;
            case R.id.floating_info:
                contextMenuAppContent.showInfo(this);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseFragment.transferAppContent = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainCourseFeedReady = false;
        mainArchiveReady = false;
    }
}