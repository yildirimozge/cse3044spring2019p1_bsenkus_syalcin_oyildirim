package com.buraksergenozge.coursediary.Data;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Fragments.CourseHourFragment;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AssignmentCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.AudioCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CourseHourCreationDialog;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.ListAdapter;
import com.buraksergenozge.coursediary.Tools.StringManager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Audio extends AppContent {
    private CourseHour courseHour;
    private File file;
    public final String name;
    public static String saveAudioPath = "";
    public static String currentAudioName;
    public static MediaRecorder mediaRecorder;
    public static boolean isRecorderActive = false;
    private static final String[] relatedFragmentTags = {CourseHourFragment.tag};


    public Audio(CourseHour courseHour, String filePath) {
        this.courseHour = courseHour;
        this.file = new File(filePath);
        String[] paths = filePath.split("/");
        this.name = paths[paths.length - 1];
    }

    public CourseHour getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(CourseHour courseHour) {
        this.courseHour = courseHour;
    }

    public File getFile() {
        return file;
    }

    public void setFilePath(String filePath) {
        file = new File(filePath);
    }

    @Override
    public int getSaveMessage() {
        return R.string.audio_save;
    }

    @Override
    public void addOperation(AppCompatActivity activity) {
        courseHour.getAudios().add(this);
    }

    @Override
    public int getDeleteMessage() {
        return R.string.audio_delete;
    }

    @Override
    public void deleteOperation(AppCompatActivity activity) {
        courseHour.getAudios().remove(this);
        file.delete();
    }

    public static boolean record(AppCompatActivity activity) {
        if (MainScreen.activeAppContent == null) {
            if (User.getCourseHoursEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else if (MainScreen.activeAppContent instanceof Semester) {
            if (((Semester)MainScreen.activeAppContent).getCourseHoursEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else if (MainScreen.activeAppContent instanceof Course) {
            if (((Course)MainScreen.activeAppContent).getCourseHours().isEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else if (MainScreen.activeAppContent instanceof Assignment) {
            if (((Assignment)MainScreen.activeAppContent).getCourse().getCourseHours().isEmpty()) {
                Toast.makeText(activity, activity.getString(R.string.no_course_hours), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (((MainScreen)activity).checkPermissions()) {
            if (MainScreen.activeAppContent instanceof CourseHour)
                saveAudioPath = createAudioFile((CourseHour) MainScreen.activeAppContent);
            else if (MainScreen.activeAppContent instanceof Note)
                saveAudioPath = createAudioFile(((Note) MainScreen.activeAppContent).getCourseHour());
            else
                saveAudioPath = createAudioFile(null);
            if (saveAudioPath == null)
                return false;
            setUpMediaRecorder();
            try {
                if (mediaRecorder == null)
                    return false;
                Log.i("burak", "BAŞLADI");
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecorderActive = true;
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            ((MainScreen)activity).requestPermissions();
            return false;
        }
        return true;
    }

    private static String createAudioFile(CourseHour courseHour) {
        String name = StringManager.getDateString(Calendar.getInstance(), "yyyyMMdd_HHmmss");
        currentAudioName = name.concat(".3gp");
        File storageDir;
        if (courseHour != null)
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ("CourseDiary/" + courseHour.getCourseHourID()));
        else
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CourseDiary");
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs())
                return null;
        }
        return storageDir.getAbsolutePath().concat("/").concat(currentAudioName);
    }

    private static void setUpMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(saveAudioPath);
    }

    public static CreationDialog getCreationDialog(int mode) {
        CreationDialog creationDialog = new AudioCreationDialog();
        creationDialog.mode = mode;
        if (mode == 0)
            creationDialog.setAppContent(new Audio(null, saveAudioPath));
        return creationDialog;
    }

    @Override
    public void edit(AppCompatActivity activity) {
    }

    @Override
    public void updateOperation(AppCompatActivity activity) {
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
    public void showInfo(AppCompatActivity activity) {
        AppContent.openCreationDialog(activity, getCreationDialog(CreationDialog.INFO_MODE));
    }

    @Override
    public String[] getRelatedFragmentTags() {
        return relatedFragmentTags;
    }

    @Override
    public String toString() {
        return name;
    }
}