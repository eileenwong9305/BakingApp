package com.example.android.baking.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.baking.data.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class IngredientTypeConverters {

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredients) {
        Gson gson = new Gson();
        return gson.toJson(ingredients);
    }
}
