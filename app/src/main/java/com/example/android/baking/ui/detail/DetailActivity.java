package com.example.android.baking.ui.detail;

import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.baking.AppExecutors;
import com.example.android.baking.BakingApplication;
import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private int recipeId = 0;
    private Recipe mRecipe;

    public static final String BUNDLE_KEY_RECIPE = "key_recipe";

    @Inject
    AppExecutors appExecutors;
    @Inject
    DetailViewModelFactory factory;
    DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ((BakingApplication) getApplication()).getAppComponent().inject(this);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.KEY_RECIPE_ID)) {
                recipeId = intent.getIntExtra(MainActivity.KEY_RECIPE_ID, 0);
            }
        }

        DetailListFragment detailListFragment = DetailListFragment.newInstance(recipeId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();

//        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(this);
//        ingredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
//        ingredientsRecyclerView.setHasFixedSize(true);
//        final IngredientAdapter ingredientAdapter = new IngredientAdapter(this);
//        ingredientsRecyclerView.setAdapter(ingredientAdapter);
//
//        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(this);
//        stepsRecyclerView.setLayoutManager(stepLayoutManager);
//        stepsRecyclerView.setHasFixedSize(true);
//        final StepAdapter stepAdapter = new StepAdapter(this);
//        stepsRecyclerView.setAdapter(stepAdapter);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
//                ingredientsRecyclerView.getContext(), ingredientLayoutManager.getOrientation());
//        ingredientsRecyclerView.addItemDecoration(dividerItemDecoration);

//        viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
//        viewModel.getRecipe(recipeId).observe(this, new Observer<Recipe>() {
//            @Override
//            public void onChanged(@Nullable Recipe recipe) {
//                if (recipe != null) {
//                    mRecipe = recipe;
//                    nameTextView.setText(recipe.getName());
//                    servingsTextView.setText(getString(R.string.serving_text, recipe.getServings()));
//                    ingredientAdapter.setIngredients(mRecipe.getIngredients());
//                    stepAdapter.setSteps(recipe.getSteps());
//                    DetailListFragment detailListFragment = new DetailListFragment();
//                    detailListFragment.setmRecipe(recipe);
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().add(R.id.detail_container, detailListFragment).commit();
//                }
//            }
//        });
    }
}
