package com.buraksergenozge.coursediary.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.User;
import com.buraksergenozge.coursediary.Fragments.CreationDialog.CreationDialog;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class CourseHourFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private RecyclerView noteRecyclerView, photoRecyclerView, audioRecyclerView;
    private CheckBox cancelCheckBox, attendedCheckBox;
    private MaterialCardView noteListHeader, photoListHeader, audioListHeader;
    private TextView courseHourTitle_TV;
    public static String tag = "courseHourFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_course_hour;
    }

    @Override
    public void open(AppContent appContent) {
        if (appContent instanceof Note) {
            childFragment = new NoteFragment();
            childFragment.parentFragment = this;
            BaseFragment.transferAppContent = appContent;
            openFragment(childFragment, NoteFragment.tag);
        }
        else if (appContent instanceof Photo) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            Uri data = Uri.parse("file://" + ((Photo)appContent).getFile().getAbsolutePath());
            intent.setDataAndType(data, "image/*");
            startActivity(intent);
        }
        else if (appContent instanceof Audio) {
            //TODO:Ses açma ekranı
        }
    }

    private void setVisibilities(boolean isNoteListEmpty, boolean isPhotoListEmpty, boolean isAudioListEmpty) {
        if(noteRecyclerView != null && photoRecyclerView != null && audioRecyclerView != null) {
            if (isNoteListEmpty)
                noteRecyclerView.setVisibility(View.GONE);
            else
                noteRecyclerView.setVisibility(View.VISIBLE);
            if (isPhotoListEmpty)
                photoRecyclerView.setVisibility(View.GONE);
            else
                photoRecyclerView.setVisibility(View.VISIBLE);
            if (isAudioListEmpty)
                audioRecyclerView.setVisibility(View.GONE);
            else
                audioRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initializeViews() {
        courseHourTitle_TV = Objects.requireNonNull(getView()).findViewById(R.id.course_hour_title_TV);
        noteListHeader = getView().findViewById(R.id.notes_cardview);
        noteListHeader.setOnClickListener(this);
        photoListHeader = getView().findViewById(R.id.photos_cardview);
        photoListHeader.setOnClickListener(this);
        audioListHeader = getView().findViewById(R.id.audios_cardview);
        audioListHeader.setOnClickListener(this);
        noteRecyclerView = getView().findViewById(R.id.notes_recyclerview);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(noteRecyclerView);
        photoRecyclerView = getView().findViewById(R.id.photos_recyclerview);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        registerForContextMenu(photoRecyclerView);
        audioRecyclerView = getView().findViewById(R.id.audios_recyclerview);
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(audioRecyclerView);
        ImageView noteAddIcon = getView().findViewById(R.id.note_add_button);
        noteAddIcon.setOnClickListener(this);
        ImageView photoAddIcon = getView().findViewById(R.id.photo_add_button);
        photoAddIcon.setOnClickListener(this);
        ImageView audioAddIcon = getView().findViewById(R.id.audio_add_button);
        audioAddIcon.setOnClickListener(this);
        cancelCheckBox = getView().findViewById(R.id.cancelCheckBox);
        cancelCheckBox.setOnCheckedChangeListener(this);
        attendedCheckBox = getView().findViewById(R.id.attendCheckBox);
        attendedCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void updateView() {
        courseHourTitle_TV.setText(appContent.toString());
        boolean isNoteListEmpty = updateRecyclerView(noteRecyclerView, ((CourseHour)appContent).getNotes());
        boolean isPhotoListEmpty = updateRecyclerView(photoRecyclerView, ((CourseHour)appContent).getPhotos());
        boolean isAudioListEmpty = updateRecyclerView(audioRecyclerView, ((CourseHour)appContent).getAudios());
        setVisibilities(isNoteListEmpty, isPhotoListEmpty, isAudioListEmpty);
        if (((CourseHour)appContent).getCancelled() == 1)
            cancelCheckBox.setChecked(true);
        else
            cancelCheckBox.setChecked(false);
        if (((CourseHour)appContent).getAttendance() == 1)
            attendedCheckBox.setChecked(true);
        else
            attendedCheckBox.setChecked(false);
        if (noteRecyclerView.getAdapter() != null) {
            if (noteRecyclerView.getAdapter().getItemCount() > 3) {
                ViewGroup.LayoutParams params = noteRecyclerView.getLayoutParams();
                params.height = 390;
                noteRecyclerView.setLayoutParams(params);
                noteRecyclerView.requestLayout();
            }
        }
        if (photoRecyclerView.getAdapter() != null) {
            if (photoRecyclerView.getAdapter().getItemCount() > 15) {
                ViewGroup.LayoutParams params = photoRecyclerView.getLayoutParams();
                params.height = 390;
                photoRecyclerView.setLayoutParams(params);
                photoRecyclerView.requestLayout();
            }
        }
        ((TextView)noteListHeader.findViewById(R.id.notes_TV)).setText(getResources().getString(R.string.notes, ((CourseHour)appContent).getNotes().size()));
        ((TextView)photoListHeader.findViewById(R.id.photos_TV)).setText(getResources().getString(R.string.photos, ((CourseHour)appContent).getPhotos().size()));
        ((TextView)audioListHeader.findViewById(R.id.audios_TV)).setText(getResources().getString(R.string.audios, ((CourseHour)appContent).getAudios().size()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_cardview:
                if (noteRecyclerView.getVisibility() == View.VISIBLE)
                    noteRecyclerView.setVisibility(View.GONE);
                else
                    noteRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.photos_cardview:
                if (photoRecyclerView.getVisibility() == View.VISIBLE)
                    photoRecyclerView.setVisibility(View.GONE);
                else
                    photoRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.audios_cardview:
                if (audioRecyclerView.getVisibility() == View.VISIBLE)
                    audioRecyclerView.setVisibility(View.GONE);
                else
                    audioRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.note_add_button:
                Note.openCreationDialog((MainScreen) Objects.requireNonNull(getActivity()), Note.getCreationDialog(CreationDialog.CREATE_MODE));
                MainScreen.activeDialog = "";
                break;
            case R.id.photo_add_button:
                Photo.takePhoto((AppCompatActivity) getActivity());
                MainScreen.activeDialog = "";
                break;
            case R.id.audio_add_button:
                // TODO: Audio.openCreationDialog((MainScreen)getActivity(), Audio.getCreationDialog());
                MainScreen.activeDialog = "";
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == cancelCheckBox) {
            attendedCheckBox.setEnabled(!b); // If cancel checkbox is checked, deactivate attended checkbox
            ((CourseHour)appContent).cancel(getContext(), b);
        }
        else if (compoundButton == attendedCheckBox) {
            if (b && ((CourseHour)appContent).getAttendance() == 0)
                User.setAttendance(getContext(), ((CourseHour)appContent), 1);
            else if (!b && ((CourseHour)appContent).getAttendance() == 1)
                User.setAttendance(getContext(), ((CourseHour)appContent), 0);
        }
    }
}