package com.buraksergenozge.coursediary.Fragments;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.buraksergenozge.coursediary.R;

public abstract class ListFragment extends BaseFragment {
    protected int layoutID;
    protected ListView contextMenuSelectedListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutID, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            getActivity().getMenuInflater().inflate(R.menu.menu_floating, menu);
        }catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        contextMenuSelectedListView = (ListView) v;
    }

    public abstract void updateView();
}
