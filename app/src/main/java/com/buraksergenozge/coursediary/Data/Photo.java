package com.buraksergenozge.coursediary.Data;

import android.arch.persistence.room.Ignore;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
    private File file;
    private static String savePhotoPath = "";
    public static String currentPhotoName = "";
    @Ignore
    private static final String[] relatedFragmentTags = {CourseHourFragment.tag};

    public Photo(CourseHour courseHour, String filePath) {
        this.courseHour = courseHour;
        this.file = new File(filePath);
    }

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public static String getSavePhotoPath() {
        return savePhotoPath;
    }

    public File getFile() {
        return file;
    }

    public void setFilePath(String filePath) {
        file = new File(filePath);
    }

    public static CreationDialog getCreationDialog(int mode) {
        CreationDialog creationDialog = new PhotoCreationDialog();
        creationDialog.mode = mode;
        if (mode == 0)
            creationDialog.setAppContent(new Photo(null, savePhotoPath));
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
    }

    public static void takePhoto(AppCompatActivity activity) {
        if (MainScreen.activeAppContent == null) {
            if (User.getCourseHoursEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if (MainScreen.activeAppContent instanceof Semester) {
            if (((Semester)MainScreen.activeAppContent).getCourseHoursEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if (MainScreen.activeAppContent instanceof Course) {
            if (((Course)MainScreen.activeAppContent).getCourseHours().isEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if (MainScreen.activeAppContent instanceof Assignment) {
            if (((Assignment)MainScreen.activeAppContent).getCourse().getCourseHours().isEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (((MainScreen)activity).checkPermissions()) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
                File photoFile;
                if (MainScreen.activeAppContent instanceof CourseHour)
                    photoFile = createPhotoFile((CourseHour) MainScreen.activeAppContent);
                else if (MainScreen.activeAppContent instanceof Note)
                    photoFile = createPhotoFile(((Note) MainScreen.activeAppContent).getCourseHour());
                else
                    photoFile = createPhotoFile(null);
                if (photoFile != null) {
                    savePhotoPath = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(activity, "com.buraksergenozge.coursediary.fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(cameraIntent, MainScreen.CAMERA_REQUEST);
                }
            }
        }
        else
            ((MainScreen)activity).requestPermissions();
    }

    private static File createPhotoFile(CourseHour courseHour) {
        String name = StringManager.getDateString(Calendar.getInstance(), "yyyyMMdd_HHmmss");
        currentPhotoName = name.concat(".jpg");
        File storageDir;
        if (courseHour != null)
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ("CourseDiary/" + courseHour.getCourseHourID()));
        else
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CourseDiary");
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs())
                return null;
        }
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
    }

    @Override
    public int getDeleteMessage() {
        return R.string.photo_deleted;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        ((CourseHour)((MainScreen)activity).getVisibleFragment().appContent).getPhotos().remove(this);
        file.delete();
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
    }

    @Override
    public void showInfo(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.INFO_MODE));
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

    @Override
    public String toString() {
        return "courseHour=" + courseHour + ", absolutePath=" + file.getAbsolutePath();
    }
}