package com.example.android.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients;
    private Context mContext;

    public IngredientAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(holder.getAdapterPosition());
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

    private String decimalFormat(float f) {
        if (f == (long) f) {
            return String.format(Locale.US, "%d", (long) f);
        } else {
            return String.format("%s", f);
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail_ingredient_tv)
        TextView ingredientTextView;
        @BindView(R.id.detail_quantity_tv)
        TextView quantityTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listIndex) {
            Ingredient ingredient = mIngredients.get(listIndex);
            ingredientTextView.setText(ingredient.getIngredient());
            quantityTextView.setText(mContext.getString(R.string.quantity_text,
                    decimalFormat(ingredient.getQuantity()),
                    ingredient.getMeasure()));
        }
    }
}
