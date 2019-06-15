package com.buraksergenozge.coursediary.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Audio;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.GradingSystem;
import com.buraksergenozge.coursediary.Data.Note;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.R;

import java.util.Objects;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
    private final MainScreen activity;
    private final ImageView listItem_IV;
    private final ImageView thumbnail_IV;
    private final TextView listItem_TV;
    private final TextView listItemSideText;
    private final TextView listItemAdditional_TV;
    private final CheckBox listItemCheckBox;
    private ItemClickListener itemClickListener;
    public AppContent appContent;

    ItemViewHolder(final View itemView, MainScreen activity) {
        super(itemView);
        this.activity = activity;
        listItem_IV = itemView.findViewById(R.id.list_item_icon);
        thumbnail_IV = itemView.findViewById(R.id.thumbnail_IV);
        listItem_TV = itemView.findViewById(R.id.listItemText);
        listItemSideText = itemView.findViewById(R.id.listItemSideText);
        listItemAdditional_TV = itemView.findViewById(R.id.listItemAdditionalText);
        listItemCheckBox = itemView.findViewById(R.id.listItemCheckBox);
        if(listItemCheckBox != null)
            listItemCheckBox.setTag(this);
        itemView.setOnClickListener(this);
        MaterialCardView materialCardView = (MaterialCardView) itemView;
        materialCardView.setOnCreateContextMenuListener(this);
    }

    void bindData(final Object viewModel) {
        this.appContent = (AppContent) viewModel;
        if (listItem_TV != null)
            listItem_TV.setText(viewModel.toString());
        if (listItemAdditional_TV != null)
            listItemAdditional_TV.setVisibility(View.GONE);
        if (viewModel instanceof Semester) {
            listItem_IV.setImageResource(R.drawable.ic_date_range_green_36dp);
            listItemSideText.setVisibility(View.GONE);
        }
        else if (viewModel instanceof Course) {
            listItem_IV.setImageResource(R.drawable.ic_class_green_36dp);
            listItemSideText.setText(((Course)viewModel).getSemester().getName());
        }
        else if (viewModel instanceof Assignment) {
            listItem_IV.setImageResource(R.drawable.ic_assignment_gray_36dp);
            listItemSideText.setText(((Assignment)viewModel).getCourse().getName());
            Objects.requireNonNull(listItemAdditional_TV).setVisibility(View.VISIBLE);
            listItemAdditional_TV.setText(StringManager.getTimeRepresentation(((Assignment) viewModel).getRemainingTimeInMillis(), activity.getResources()));
            if (((Assignment)viewModel).getRemainingTimeInMillis() < 86400000)
                listItemAdditional_TV.setTextColor(Color.rgb(255,0,0));
        }
        else if (viewModel instanceof CourseHour) {
            listItem_IV.setImageResource(R.drawable.ic_schedule_gray_36dp);
            listItemSideText.setText(((CourseHour)viewModel).getCourse().getName());
            if (((CourseHour) viewModel).getAttendance() == 1)
                listItemCheckBox.setChecked(true);
            else
                listItemCheckBox.setChecked(false);
            if (((CourseHour) viewModel).getCancelled() == 1)
                listItemCheckBox.setEnabled(false);
            else
                listItemCheckBox.setEnabled(true);
        }
        else if (viewModel instanceof Note) {
            listItem_IV.setImageResource(R.drawable.ic_note_gray_36dp);
            listItemSideText.setText(((Note)viewModel).getCourseHour().toString());
        }
        else if (viewModel instanceof Audio) {
            listItem_IV.setImageResource(R.drawable.ic_audiotrack_green_36dp);
            listItemSideText.setVisibility(View.GONE);
        }
        else if (viewModel instanceof GradingSystem) {
            listItem_IV.setImageResource(R.drawable.ic_assignment_turned_in_green_36dp);
            listItemSideText.setVisibility(View.GONE);
        }
        else if (viewModel instanceof Photo) {
            Bitmap myBitmap = BitmapFactory.decodeFile(((Photo)viewModel).getFile().getAbsolutePath());
            thumbnail_IV.setImageBitmap(myBitmap);
        }
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(getAdapterPosition());
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        Objects.requireNonNull(activity).getMenuInflater().inflate(R.menu.menu_floating, contextMenu);
        MainScreen.contextMenuAppContent = appContent;
    }
}