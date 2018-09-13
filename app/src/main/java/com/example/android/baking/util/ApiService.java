package com.example.android.baking.util;

import com.example.android.baking.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
