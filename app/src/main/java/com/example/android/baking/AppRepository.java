package com.example.android.baking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.database.RecipeDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AppRepository {

    private final MutableLiveData<List<Recipe>> recipeList;
    private final ApiService apiService;
    private final RecipeDao recipeDao;
    private final AppExecutors appExecutors;

    @Inject
    public AppRepository(ApiService apiService, RecipeDao recipeDao, AppExecutors appExecutors) {
        this.apiService = apiService;
        this.recipeDao = recipeDao;
        this.appExecutors = appExecutors;
        recipeList = new MutableLiveData<>();
    }

    public void loadRecipe(){
        apiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                final List<Recipe> recipes = response.body();
                final List<Ingredient> ingredients = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    List<Ingredient> ingredient = recipe.getIngredients();
                }

                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        recipeDao.insertRecipe(recipes);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(AppRepository.class.getSimpleName(), t.getMessage());
            }
        });
    }

    public LiveData<List<Recipe>> getRecipeList(){
        loadRecipe();
        return recipeDao.loadAllRecipes();
    }

    public LiveData<Recipe> getRecipe(int recipeId) {
        return recipeDao.getRecipe(recipeId);
    }

}
