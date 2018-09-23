package com.example.android.baking.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.databinding.FragmentDetailListBinding;

import org.parceler.Parcels;

public class DetailListFragment extends Fragment implements StepAdapter.StepItemClickListener {

    private static final String ARG_KEY_RECIPE = "arg_recipe";
    private static final String BUNDLE_KEY_RECIPE = "bundle_recipe";
    private Recipe mRecipe;
    private OnStepClickListener mStepClickListener;
    private FragmentDetailListBinding mViewBinding;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RECIPE)) {
            mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_KEY_RECIPE));
        } else {
            mRecipe = getRecipe();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail_list, container, false);
        mViewBinding = FragmentDetailListBinding.bind(rootView);
        DetailViewModel viewModel = DetailActivity.obtainViewModel(getActivity());
        mViewBinding.setViewmodel(viewModel);

        setupRecyclerView();

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_KEY_RECIPE, Parcels.wrap(mRecipe));
    }

    private void setupRecyclerView() {
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mViewBinding.ingredientsRv.getContext(), ingredientLayoutManager.getOrientation());
        mViewBinding.ingredientsRv.setLayoutManager(ingredientLayoutManager);
        mViewBinding.ingredientsRv.setHasFixedSize(true);
        mViewBinding.ingredientsRv.addItemDecoration(dividerItemDecoration);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(mRecipe.getIngredients());
        mViewBinding.ingredientsRv.setAdapter(ingredientAdapter);

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        mViewBinding.stepsRv.setLayoutManager(stepLayoutManager);
        mViewBinding.stepsRv.setHasFixedSize(true);
        StepAdapter stepAdapter = new StepAdapter(this, mRecipe.getSteps());
        mViewBinding.stepsRv.setAdapter(stepAdapter);
    }

    @Override
    public void onClick(int position) {
        mStepClickListener.onStepSelected(position);
    }

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

}
