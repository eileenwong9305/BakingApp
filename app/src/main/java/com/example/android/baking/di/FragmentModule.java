package com.example.android.baking.di;

import com.example.android.baking.ui.detail.DetailListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract DetailListFragment contributeDetailListFragment();
}
