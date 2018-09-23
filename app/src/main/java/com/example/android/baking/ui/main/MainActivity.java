package com.example.android.baking.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.databinding.ActivityMainBinding;
import com.example.android.baking.ui.detail.DetailActivity;
import com.example.android.baking.util.SimpleIdlingResource;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_RECIPE = "recipe";

    @Inject
    MainViewModelFactory mViewModelFactory;
    MainViewModel mViewModel;
    private RecipeAdapter mAdapter;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    private ActivityMainBinding mBinding;

    private ListItemClickListener mListener = new ListItemClickListener() {
        @Override
        public void onClick(Recipe recipe) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(KEY_RECIPE, Parcels.wrap(recipe));
            startActivity(intent);
        }
    };

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rv.setHasFixedSize(true);
        mAdapter = new RecipeAdapter(mListener);
        mBinding.rv.setAdapter(mAdapter);

        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
        mViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && recipes.size() != 0) {
                    mBinding.setIsLoading(false);
                    mAdapter.setRecipes(recipes);
                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }
}
