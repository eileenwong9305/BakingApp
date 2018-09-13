package com.example.android.baking.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.baking.util.AppRepository;

import javax.inject.Inject;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppRepository repository;

    @Inject
    public MainViewModelFactory(AppRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(repository);
    }
}
