package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.Ignore;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//import androidx.annotation.RecentlyNonNull;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseHourCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.PhotoCreationDialog;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.Tools.StringManager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Photo extends AppContent{
    private CourseHour courseHour;
    private String absolutePath;
    private static String currentPhotoPath = "";
    public static String currentPhotoName = "";
    @Ignore
    private static String[] relatedFragmentTags = {CourseHourFragment.tag};


    public Photo(CourseHour courseHour, String absolutePath) {
        this.courseHour = courseHour;
        this.absolutePath = absolutePath;
    }

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public static String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public static void setCurrentPhotoPath(String currentPhotoPath) {
        Photo.currentPhotoPath = currentPhotoPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public static DialogFragment getCreationDialog() {
        return new PhotoCreationDialog();
    }

    @Override
    public void edit(AppCompatActivity activity) {
        //AppContent.openCreationDialog(activity, getCreationDialog(true));
    }

    public static void takePhoto(AppCompatActivity activity) {
        if (User.getCourseHoursEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
            return;
        }
        if (((MainScreen)activity).checkPermissions()) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
                File photoFile;
                photoFile = createPhotoFile(activity);
                if (photoFile != null) {
                    currentPhotoPath = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(activity, "com.buraksergenozge.coursediary.fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(cameraIntent, MainScreen.CAMERA_REQUEST);
                }
            }
        }
    }

    private static File createPhotoFile(AppCompatActivity activity) {
        String name = StringManager.getDateString(Calendar.getInstance(), "yyyyMMdd_HHmmss");
        currentPhotoName = name.concat(".jpg");
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CourseDiary");
        if (!storageDir.exists())
            storageDir.mkdir();
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public int getSaveMessage() {
        return R.string.photo_saved;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        courseHour.getPhotos().add(this);
        MainScreen.integrateData(activity);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.photo_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        courseHour.getPhotos().remove(this);
        File file = new File(absolutePath);
        file.delete();
        MainScreen.activeAppContent = courseHour;
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
    }

    @Override
    public void showInfo(AppCompatActivity activity) {
        Toast.makeText(activity, toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillSpinners(CreationDialog creationDialog) {
        boolean isChanged = creationDialog.selectSemesterOnSpinner(courseHour.getCourse().getSemester());
        creationDialog.selectedSemester = (Semester)creationDialog.semesterSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof CourseCreationDialog)
            return;
        if (isChanged || creationDialog.courseSelectionSpinner.getAdapter() == null) {
            ListAdapter<Course> adapter = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedSemester.getCourses(), R.layout.list_item);
            creationDialog.courseSelectionSpinner.setAdapter(adapter);
        }
        isChanged = creationDialog.selectCourseOnSpinner(courseHour.getCourse());
        creationDialog.selectedCourse = (Course) creationDialog.courseSelectionSpinner.getSelectedItem();
        if (creationDialog instanceof AssignmentCreationDialog || creationDialog instanceof CourseHourCreationDialog)
            return;
        if (isChanged || creationDialog.courseHourSelectionSpinner.getAdapter() == null) {
            ListAdapter<CourseHour> adapter2 = new ListAdapter<>(creationDialog.getActivity(), creationDialog.selectedCourse.getCourseHours(), R.layout.list_item);
            creationDialog.courseHourSelectionSpinner.setAdapter(adapter2);
        }
        creationDialog.selectCourseHourOnSpinner(courseHour);
        creationDialog.selectedCourseHour = (CourseHour) creationDialog.courseHourSelectionSpinner.getSelectedItem();
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    //@RecentlyNonNull
    @Override
    public String toString() {
        return "courseHour=" + courseHour + ", absolutePath=" + absolutePath;
    }
}