package com.example.android.baking.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListFragment extends Fragment implements StepAdapter.StepItemClickListener {

    private static final String ARG_KEY_RECIPE = "arg_recipe";
    private static final String BUNDLE_KEY_RECIPE = "bundle_recipe";
    @BindView(R.id.ingredients_rv)
    public RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.detail_name_tv)
    public TextView mNameTextView;
    @BindView(R.id.detail_servings_tv)
    public TextView mServingsTextView;
    @BindView(R.id.steps_rv)
    public RecyclerView mStepsRecyclerView;
    private Recipe mRecipe;
    private OnStepClickListener mStepClickListener;

    public DetailListFragment() {
    }

    public static DetailListFragment newInstance(Recipe recipe) {
        DetailListFragment detailListFragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_KEY_RECIPE, Parcels.wrap(recipe));
        detailListFragment.setArguments(bundle);
        return detailListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mStepClickListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnStepClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mIngredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(getActivity());
        mIngredientsRecyclerView.setAdapter(ingredientAdapter);

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        mStepsRecyclerView.setLayoutManager(stepLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
        final StepAdapter stepAdapter = new StepAdapter(this);
        mStepsRecyclerView.setAdapter(stepAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mIngredientsRecyclerView.getContext(), ingredientLayoutManager.getOrientation());
        mIngredientsRecyclerView.addItemDecoration(dividerItemDecoration);

        mNameTextView.setText(mRecipe.getName());
        mServingsTextView.setText(getString(R.string.serving_text, mRecipe.getServings()));
        ingredientAdapter.setIngredients(mRecipe.getIngredients());
        stepAdapter.setSteps(mRecipe.getSteps());

        return rootView;
    }

    private Recipe getRecipe() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return Parcels.unwrap(bundle.getParcelable(ARG_KEY_RECIPE));
        }
        return null;
    }

    @Override
    public void onClick(int position) {
        mStepClickListener.onStepSelected(position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_KEY_RECIPE, Parcels.wrap(mRecipe));
    }

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

}
