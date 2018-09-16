package com.example.android.baking.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.baking.util.ApiService;
import com.example.android.baking.R;
import com.example.android.baking.adapter.RecipeAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.util.SimpleIdlingResource;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    public static final String KEY_RECIPE = "recipe";

    private RecipeAdapter adapter;

    @BindView(R.id.rv)
    public RecyclerView recyclerView;

    @Inject
    MainViewModelFactory viewModelFactory;
    MainViewModel viewModel;

    @Inject
    ApiService apiService;

    @Nullable private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecipeAdapter(this, this);
        recyclerView.setAdapter(adapter);



        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size()!=0) {
                    Log.e(getClass().getSimpleName(), "recipe not null");
                    adapter.setRecipes(recipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }

            }
        });
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_RECIPE, recipe);
        startActivity(intent);
    }
}
