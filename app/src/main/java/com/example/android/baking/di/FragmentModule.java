package com.example.android.baking.di;

import com.example.android.baking.ui.detail.DetailListFragment;
import com.example.android.baking.ui.step.StepDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract DetailListFragment contributeDetailListFragment();

    @ContributesAndroidInjector
    abstract StepDetailFragment contributeStepDetailFragment();
}
