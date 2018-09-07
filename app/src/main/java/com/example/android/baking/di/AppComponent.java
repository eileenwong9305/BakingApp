package com.example.android.baking.di;

import android.app.Application;

import com.example.android.baking.BakingApplication;
import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.ui.detail.DetailListFragment;
import com.example.android.baking.ui.detail.DetailViewModel;
import com.example.android.baking.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(BakingApplication bakingApplication);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(DetailListFragment detailListFragment);

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance
        Builder application(Application application);
    }


}
