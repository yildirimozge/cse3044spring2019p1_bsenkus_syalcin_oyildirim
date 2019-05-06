package com.buraksergenozge.coursediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.buraksergenozge.coursediary.Fragments.Archive;
import com.buraksergenozge.coursediary.Fragments.CourseFeed;

public class PagerAdapter extends FragmentStatePagerAdapter {
    CourseFeed courseFeed;
    Archive archive;
    int noOfTabs;
    public PagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        courseFeed = new CourseFeed();
        archive = new Archive();
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return courseFeed;
            case 1:
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