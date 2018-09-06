package com.example.android.baking.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.baking.AppRepository;
import com.example.android.baking.ui.main.MainViewModel;

import javax.inject.Inject;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppRepository repository;

    @Inject
    public DetailViewModelFactory(AppRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(repository);
    }
}
