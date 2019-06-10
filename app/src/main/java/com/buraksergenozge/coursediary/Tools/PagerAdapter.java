package com.buraksergenozge.coursediary.Tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buraksergenozge.coursediary.Fragments.MainArchive;
import com.buraksergenozge.coursediary.Fragments.MainCourseFeed;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private MainCourseFeed mainCourseFeed;
    private MainArchive mainArchive;
    private int noOfTabs;
    public PagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        mainCourseFeed = new MainCourseFeed();
        mainArchive = new MainArchive();
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mainCourseFeed;
            case 1:
                return mainArchive;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}