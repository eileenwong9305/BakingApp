package com.example.android.baking.util;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.database.RecipeDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AppRepository {

    private final ApiService mApiService;
    private final RecipeDao mRecipeDao;
    private final AppExecutors mAppExecutors;

    @Inject
    public AppRepository(ApiService apiService, RecipeDao recipeDao, AppExecutors appExecutors) {
        this.mApiService = apiService;
        this.mRecipeDao = recipeDao;
        this.mAppExecutors = appExecutors;
    }

    public void loadRecipe() {
        mApiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                final List<Recipe> recipes = response.body();
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mRecipeDao.insertRecipe(recipes);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(AppRepository.class.getSimpleName(), t.getMessage());
            }
        });
    }

    public LiveData<List<Recipe>> getRecipeList() {
        loadRecipe();
        return mRecipeDao.loadAllRecipes();
    }

    public Recipe getRecipe(int recipeId) {
        return mRecipeDao.getRecipe(recipeId);
    }

}
