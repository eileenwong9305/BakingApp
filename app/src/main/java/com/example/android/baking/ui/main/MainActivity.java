package com.example.android.baking.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.baking.ApiService;
import com.example.android.baking.BakingApplication;
import com.example.android.baking.R;
import com.example.android.baking.adapter.RecipeAdapter;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.detail.DetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    public static final String KEY_RECIPE_ID = "recipe_id";

    private RecipeAdapter adapter;

    @BindView(R.id.rv)
    public RecyclerView recyclerView;

    @Inject
    MainViewModelFactory viewModelFactory;
    MainViewModel viewModel;

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((BakingApplication) getApplication()).getAppComponent().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RecipeAdapter(this, this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
