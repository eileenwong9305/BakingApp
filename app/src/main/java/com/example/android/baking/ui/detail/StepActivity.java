package com.example.android.baking.ui.detail;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.baking.R;
import com.example.android.baking.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends FragmentActivity {

    private int stepNumber;
    private List<Step> steps;

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
            if (intent.hasExtra(DetailListFragment.KEY_STEP_NUMBER)){
                stepNumber = intent.getIntExtra(DetailListFragment.KEY_STEP_NUMBER, 0);
            }
            if (intent.hasExtra(DetailListFragment.KEY_STEPS)) {
                steps = intent.getParcelableArrayListExtra(DetailListFragment.KEY_STEPS);
            }
        }

        stepCollectionPagerAdapter = new StepCollectionPagerAdapter(getSupportFragmentManager(), steps);
        viewPager.setAdapter(stepCollectionPagerAdapter);
        viewPager.setCurrentItem(stepNumber);
    }
}
