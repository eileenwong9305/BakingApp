package com.example.android.baking.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import javax.inject.Singleton;

@Singleton
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({IngredientTypeConverters.class, StepTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipe";

    public abstract RecipeDao recipeDao();

}
