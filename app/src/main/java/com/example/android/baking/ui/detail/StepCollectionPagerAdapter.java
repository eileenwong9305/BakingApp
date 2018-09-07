package com.example.android.baking.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.baking.data.Step;

import java.util.List;

public class StepCollectionPagerAdapter extends FragmentStatePagerAdapter {

    public static final String BUNDLE_KEY_DESC = "key_description";
    public static final String BUNDLE_KEY_VIDEO_URL = "key_video_url";
    private List<Step> steps;

    public StepCollectionPagerAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_DESC, steps.get(position).getDescription());
        bundle.putString(BUNDLE_KEY_VIDEO_URL, steps.get(position).getVideoURL());
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);
        return stepDetailFragment;
    }

    @Override
    public int getCount() {
        return steps.size();
    }
}
