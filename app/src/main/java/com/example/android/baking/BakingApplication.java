package com.example.android.baking;

import android.app.Application;

import com.example.android.baking.di.AppComponent;
import com.example.android.baking.di.AppModule;
import com.example.android.baking.di.DaggerAppComponent;

public class BakingApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
        appComponent = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
