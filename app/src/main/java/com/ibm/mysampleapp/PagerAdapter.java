package com.ibm.mysampleapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int NoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumbersOfTabs) {
        super(fm);
        this.NoOfTabs = NumbersOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Tab tab = new Tab();
                return tab;
            case 1:
                Tab1 tab1 = new Tab1();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NoOfTabs;
    }
}
