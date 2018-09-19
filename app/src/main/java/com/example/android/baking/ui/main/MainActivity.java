package com.example.android.baking.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.adapter.RecipeAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.util.ApiService;
import com.example.android.baking.util.SimpleIdlingResource;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    public static final String KEY_RECIPE = "recipe";
    @BindView(R.id.rv)
    public RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    public ProgressBar mProgressBar;
    @BindView(R.id.error_message)
    public TextView mErrorMessage;

    @Inject
    MainViewModelFactory viewModelFactory;
    MainViewModel viewModel;
    private RecipeAdapter adapter;
    @Nullable
    private SimpleIdlingResource idlingResource;

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

        showLoading();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(adapter);

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
                if (recipes != null && recipes.size() != 0) {
                    adapter.setRecipes(recipes);
                    showContent();
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                } else {
//                    showError();
                }

            }
        });
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_RECIPE, Parcels.wrap(recipe));
        startActivity(intent);
    }

    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showContent() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
