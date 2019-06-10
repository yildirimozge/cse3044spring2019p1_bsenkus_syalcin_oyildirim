package com.buraksergenozge.coursediary.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buraksergenozge.coursediary.Data.Assignment;
import com.buraksergenozge.coursediary.Data.Course;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Semester;
import com.buraksergenozge.coursediary.R;

import java.util.List;

public class ListAdapter<T> extends BaseAdapter {
    private Context context; //context
    private List<T> items; //data source of the list adapter
    private int listItemTypeID;

    public ListAdapter(Context context, List<T> items, int listItemTypeID) {
        this.context = context;
        this.items = items;
        this.listItemTypeID = listItemTypeID;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public T getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(listItemTypeID, parent, false);
        }
        final T currentItem = getItem(position);
        final TextView listItem_TV = convertView.findViewById(R.id.listItemText);
        listItem_TV.setText(currentItem.toString());
        ImageView listItem_IV = convertView.findViewById(R.id.list_item_icon);
        TextView listItemAdditionalText_TV = convertView.findViewById(R.id.listItemAdditionalText);
        TextView listItemSideText_TV = convertView.findViewById(R.id.listItemSideText);
        if (currentItem instanceof Semester)
            listItem_IV.setImageResource(R.drawable.ic_calendar_month_grey600_36dp);
        else if (currentItem instanceof Course)
            listItem_IV.setImageResource(R.drawable.ic_bag_personal_outline_grey600_36dp);
        else if (currentItem instanceof Assignment)
            listItem_IV.setImageResource(R.drawable.ic_notebook_grey600_36dp);
        else if (currentItem instanceof CourseHour)
            listItem_IV.setImageResource(R.drawable.ic_timelapse_grey600_36dp);
        listItemSideText_TV.setVisibility(View.GONE);
        listItemAdditionalText_TV.setVisibility(View.GONE);
        return convertView;
    }
}