package com.example.android.baking.ui.detail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.BakingApplication;
import com.example.android.baking.R;
import com.example.android.baking.adapter.IngredientAdapter;
import com.example.android.baking.adapter.StepAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListFragment extends Fragment implements StepAdapter.StepItemClickListener {

    @Inject
    DetailViewModelFactory factory;

    @BindView(R.id.ingredients_rv)
    public RecyclerView ingredientsRecyclerView;
    @BindView(R.id.detail_name_tv)
    public TextView nameTextView;
    @BindView(R.id.detail_servings_tv)
    public TextView servingsTextView;
    @BindView(R.id.steps_rv)
    public RecyclerView stepsRecyclerView;

    public static final String KEY_VIDEO_URL = "video_url";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";
    public static final String BUNDLE_KEY_RECIPE_ID = "key_recipe_id";

    private Recipe mRecipe;
    private int recipeId;

    public DetailListFragment() {}

    public static DetailListFragment newInstance(int recipeId) {
        DetailListFragment detailListFragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_RECIPE_ID, recipeId);
        detailListFragment.setArguments(bundle);
        return detailListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_list, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity());
        ingredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientsRecyclerView.setHasFixedSize(true);
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(getActivity());
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        stepsRecyclerView.setLayoutManager(stepLayoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        final StepAdapter stepAdapter = new StepAdapter(getActivity(), this);
        stepsRecyclerView.setAdapter(stepAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                ingredientsRecyclerView.getContext(), ingredientLayoutManager.getOrientation());
        ingredientsRecyclerView.addItemDecoration(dividerItemDecoration);


        DetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
        viewModel.getRecipe(getRecipeId()).observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    mRecipe = recipe;
                    nameTextView.setText(mRecipe.getName());
                    servingsTextView.setText(getString(R.string.serving_text, mRecipe.getServings()));
                    ingredientAdapter.setIngredients(mRecipe.getIngredients());
                    stepAdapter.setSteps(mRecipe.getSteps());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BakingApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    public void setmRecipe(Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    private int getRecipeId() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getInt(BUNDLE_KEY_RECIPE_ID, -1);
        }
        return -1;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), StepActivity.class);
//        intent.putExtra(KEY_VIDEO_URL, step.getVideoURL());
//        intent.putExtra(KEY_DESCRIPTION, step.getDescription());
        intent.putParcelableArrayListExtra(KEY_STEPS, (ArrayList) mRecipe.getSteps());
        intent.putExtra(KEY_STEP_NUMBER, position);
        startActivity(intent);
    }
}
