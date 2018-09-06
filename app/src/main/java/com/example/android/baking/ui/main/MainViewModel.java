package com.example.android.baking.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.baking.AppRepository;
import com.example.android.baking.data.Recipe;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipes;

    @Inject
    public MainViewModel(AppRepository repository) {
        recipes = repository.getRecipeList();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
