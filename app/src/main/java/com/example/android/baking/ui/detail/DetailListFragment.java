package com.example.android.baking.ui.detail;

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

import com.example.android.baking.AppExecutors;
import com.example.android.baking.BakingApplication;
import com.example.android.baking.R;
import com.example.android.baking.adapter.IngredientAdapter;
import com.example.android.baking.adapter.StepAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListFragment extends Fragment implements StepAdapter.StepItemClickListener {

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

    private Recipe recipe;

    public DetailListFragment() {}

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

        nameTextView.setText(recipe.getName());
        servingsTextView.setText(getString(R.string.serving_text, recipe.getServings()));
        ingredientAdapter.setIngredients(recipe.getIngredients());
        stepAdapter.setSteps(recipe.getSteps());

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onClick(Step step) {
        Intent intent = new Intent(getActivity(), StepActivity.class);
        intent.putExtra(KEY_VIDEO_URL, step.getVideoURL());
        intent.putExtra(KEY_DESCRIPTION, step.getDescription());
        startActivity(intent);
    }
}
