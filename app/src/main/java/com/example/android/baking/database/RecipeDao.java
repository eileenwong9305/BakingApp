package com.example.android.baking.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.baking.data.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(List<Recipe> recipes);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    LiveData<Recipe> getRecipes(int recipeId);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    Recipe getRecipe(int recipeId);

}
