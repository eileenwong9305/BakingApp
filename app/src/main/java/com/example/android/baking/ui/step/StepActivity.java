package com.example.android.baking.ui.step;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.detail.DetailActivity;
import com.rd.PageIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class StepActivity extends AppCompatActivity {

    private int stepNumber;
    private List<Step> steps;

    StepCollectionPagerAdapter stepCollectionPagerAdapter;
    @BindView(R.id.pager)
    public ViewPager viewPager;
    @BindView(R.id.page_indicator_view)
    public PageIndicatorView pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(DetailActivity.KEY_STEP_NUMBER)){
                stepNumber = intent.getIntExtra(DetailActivity.KEY_STEP_NUMBER, 0);
            }
            if (intent.hasExtra(DetailActivity.KEY_STEPS)) {
                steps = intent.getParcelableArrayListExtra(DetailActivity.KEY_STEPS);
            }
        }

        stepCollectionPagerAdapter = new StepCollectionPagerAdapter(getSupportFragmentManager(), steps);
        viewPager.setAdapter(stepCollectionPagerAdapter);
        viewPager.setCurrentItem(stepNumber);

    }
}
