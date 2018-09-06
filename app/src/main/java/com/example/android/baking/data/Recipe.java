package com.example.android.baking.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.android.baking.database.IngredientTypeConverters;
import com.example.android.baking.database.StepTypeConverters;

import java.util.List;

@Entity
public class Recipe {

    @PrimaryKey
    @NonNull
    private int id;
    private String name;
    @TypeConverters(IngredientTypeConverters.class)
    private List<Ingredient> ingredients = null;
    @TypeConverters(StepTypeConverters.class)
    private List<Step> steps = null;
    private int servings;
    private String image;

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings,
                  String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
