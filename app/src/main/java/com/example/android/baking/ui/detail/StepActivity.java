package com.example.android.baking.ui.detail;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends FragmentActivity {

    private String videoUrl;
    private String description;

    StepCollectionPagerAdapter stepCollectionPagerAdapter;
    @BindView(R.id.pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(DetailListFragment.KEY_VIDEO_URL)){
                videoUrl = intent.getStringExtra(DetailListFragment.KEY_VIDEO_URL);
            }
            if (intent.hasExtra(DetailListFragment.KEY_DESCRIPTION)){
                description = intent.getStringExtra(DetailListFragment.KEY_DESCRIPTION);
            }
        }

        stepCollectionPagerAdapter = new StepCollectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(stepCollectionPagerAdapter);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setDescription(description);
        stepDetailFragment.setVideoUrl(videoUrl);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_container, stepDetailFragment).commit();
    }
}
