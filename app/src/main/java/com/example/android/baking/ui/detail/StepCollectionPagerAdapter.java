package com.example.android.baking.ui.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class StepCollectionPagerAdapter extends FragmentStatePagerAdapter {

    public StepCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new DetailListFragment();
    }

    @Override
    public int getCount() {
        return 100;
    }
}
