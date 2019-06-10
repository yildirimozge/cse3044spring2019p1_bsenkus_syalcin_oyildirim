package com.buraksergenozge.coursediary.Fragments;

import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.R;
import com.buraksergenozge.coursediary.Tools.StringManager;

import java.util.Objects;

public class NoteFragment extends BaseFragment {
    private TextView noteTitle_TV, noteCreateDate_TV, noteText_TV;
    public static String tag = "noteFragment";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_note;
    }

    @Override
    public void initializeViews() {
        noteTitle_TV = Objects.requireNonNull(getView()).findViewById(R.id.noteTitle_TV);
        noteCreateDate_TV = getView().findViewById(R.id.noteCreateDate_TV);
        noteText_TV = getView().findViewById(R.id.note_content_TV);
    }

    @Override
    public void updateView() {
        noteTitle_TV.setText(((Note)appContent).getTitle());
        noteCreateDate_TV.setText(StringManager.getDateString(((Note)appContent).getCreateDate(), "EEE, d MMM yyyy HH:mm"));
        noteText_TV.setText(((Note)appContent).getText());
    }

    @Override
    public void open(AppContent appContent) {

    }
}