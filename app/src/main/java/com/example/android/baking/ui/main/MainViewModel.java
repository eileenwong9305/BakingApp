package com.example.android.baking.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.util.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private LiveData<List<Recipe>> mRecipes;

    @Inject
    public MainViewModel(AppRepository repository) {
        mRecipes = repository.getRecipeList();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
}
