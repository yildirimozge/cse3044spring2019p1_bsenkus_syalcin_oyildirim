package com.buraksergenozge.coursediary.Tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buraksergenozge.coursediary.Activities.MainScreen;
import com.buraksergenozge.coursediary.Data.AppContent;
import com.buraksergenozge.coursediary.Data.CourseHour;
import com.buraksergenozge.coursediary.Data.Photo;
import com.buraksergenozge.coursediary.Fragments.BaseFragment;
import com.buraksergenozge.coursediary.R;

import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter {
    private List<T> items;
    private MainScreen activity;
    private BaseFragment fragment;

    public RecyclerAdapter(BaseFragment fragment, List<T> items, MainScreen activity) {
        this.fragment = fragment;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        return new ItemViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemViewHolder) viewHolder).bindData(items.get(i));
        ((ItemViewHolder) viewHolder).setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                fragment.open((AppContent) items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() > 0 && items.get(0) instanceof CourseHour)
            return R.layout.list_item2;
        else if (items.size() > 0 && items.get(0) instanceof Photo)
            return R.layout.thumbnail;
        else
            return R.layout.list_item;
    }
}