package com.example.android.baking.di;

import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.ui.detail.DetailListFragment;
import com.example.android.baking.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract DetailActivity contributeDetailActivity();
}
