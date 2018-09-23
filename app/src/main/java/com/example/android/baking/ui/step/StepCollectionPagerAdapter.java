package com.example.android.baking.ui.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.baking.data.Step;

import org.parceler.Parcels;

import java.util.List;

public class StepCollectionPagerAdapter extends FragmentStatePagerAdapter {

    public static final String BUNDLE_KEY_STEP = "key_step";
    private List<Step> mSteps;

    public StepCollectionPagerAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        this.mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_STEP, Parcels.wrap(mSteps.get(position)));
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        return stepDetailFragment;
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Preparation";
        return String.valueOf(position);
    }
}
