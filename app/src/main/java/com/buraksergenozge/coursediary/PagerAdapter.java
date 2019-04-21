package com.buraksergenozge.coursediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buraksergenozge.coursediary.Activities.Archive;
import com.buraksergenozge.coursediary.Activities.CourseFeed;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int noOfTabs;
    public PagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CourseFeed courseFeed = new CourseFeed();
                return courseFeed;
            case 1:
                Archive archive = new Archive();
                return archive;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}