package com.example.android.baking.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.baking.AppRepository;
import com.example.android.baking.data.Recipe;

public class DetailViewModel extends ViewModel {

    private AppRepository repository;

    public DetailViewModel(AppRepository repository) {
        this.repository = repository;
    }

    public LiveData<Recipe> getRecipe(int recipeId) {
        return repository.getRecipe(recipeId);
    }
}
