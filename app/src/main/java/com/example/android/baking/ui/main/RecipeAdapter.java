package com.example.android.baking.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.databinding.ListItemBinding;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private ListItemClickListener mClickListener;

    public RecipeAdapter(ListItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item, parent, false);
        binding.setListener(mClickListener);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.binding.setRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        final ListItemBinding binding;

        public RecipeViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
