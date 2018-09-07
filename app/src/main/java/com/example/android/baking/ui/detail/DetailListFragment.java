package com.example.android.baking.ui.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.adapter.IngredientAdapter;
import com.example.android.baking.adapter.StepAdapter;
import com.example.android.baking.data.Recipe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class DetailListFragment extends Fragment implements StepAdapter.StepItemClickListener {

    @BindView(R.id.ingredients_rv)
    public RecyclerView ingredientsRecyclerView;
    @BindView(R.id.detail_name_tv)
    public TextView nameTextView;
    @BindView(R.id.detail_servings_tv)
    public TextView servingsTextView;
    @BindView(R.id.steps_rv)
    public RecyclerView stepsRecyclerView;

    public static final String KEY_STEPS = "steps";
    public static final String KEY_STEP_NUMBER = "step_number";
    private static final String ARG_KEY_RECIPE = "arg_recipe";
    private static final String BUNDLE_KEY_RECIPE = "bundle_recipe";

    private Recipe mRecipe;

    public DetailListFragment() {}

    public static DetailListFragment newInstance(Recipe recipe) {
        DetailListFragment detailListFragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_KEY_RECIPE, recipe);
        detailListFragment.setArguments(bundle);
        return detailListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(BUNDLE_KEY_RECIPE);
        } else {
            mRecipe = getRecipe();
        }

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

        nameTextView.setText(mRecipe.getName());
        servingsTextView.setText(getString(R.string.serving_text, mRecipe.getServings()));
        ingredientAdapter.setIngredients(mRecipe.getIngredients());
        stepAdapter.setSteps(mRecipe.getSteps());

        return rootView;
    }

    private Recipe getRecipe() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getParcelable(ARG_KEY_RECIPE);
        }
        return null;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putParcelableArrayListExtra(KEY_STEPS, (ArrayList) mRecipe.getSteps());
        intent.putExtra(KEY_STEP_NUMBER, position);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_KEY_RECIPE, mRecipe);
    }

}
