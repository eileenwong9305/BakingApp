package com.example.android.baking.di;

import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.ui.main.MainActivity;
import com.example.android.baking.ui.step.StepActivity;
import com.example.android.baking.widget.ListWidgetService;
import com.example.android.baking.widget.ShowIngredientService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract DetailActivity contributeDetailActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract StepActivity contributeStepActivity();

    @ContributesAndroidInjector
    abstract ShowIngredientService contributeShowIngredientService();

    @ContributesAndroidInjector
    abstract ListWidgetService contributeListWidgetService();
}
