package com.example.android.baking.ui.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.detail.DetailActivity;

import org.parceler.Parcels;

import java.util.List;

public class StepActivity extends AppCompatActivity {

    StepCollectionPagerAdapter mStepCollectionPagerAdapter;
    private int mStepNumber;
    private List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(DetailActivity.KEY_STEP_NUMBER)) {
                mStepNumber = intent.getIntExtra(DetailActivity.KEY_STEP_NUMBER, 0);
            }
            if (intent.hasExtra(DetailActivity.KEY_STEPS)) {
                mSteps = Parcels.unwrap(intent.getParcelableExtra(DetailActivity.KEY_STEPS));
            }
        }

        mStepCollectionPagerAdapter = new StepCollectionPagerAdapter(getSupportFragmentManager(), mSteps);
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(mStepCollectionPagerAdapter);
        viewPager.setCurrentItem(mStepNumber);
    }
}
