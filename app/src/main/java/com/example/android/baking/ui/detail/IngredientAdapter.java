package com.example.android.baking.ui.detail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.databinding.ListIngredientItemBinding;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListIngredientItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.list_ingredient_item, parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.binding.setIngredient(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        final ListIngredientItemBinding binding;

        public IngredientViewHolder(ListIngredientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
