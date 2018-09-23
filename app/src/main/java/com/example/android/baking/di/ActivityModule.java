package com.example.android.baking.di;

import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.widget.ListWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract ListWidgetService contributeListWidgetService();
}
