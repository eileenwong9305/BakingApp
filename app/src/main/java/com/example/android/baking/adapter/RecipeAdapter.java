package com.example.android.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;
    private ListItemClickListener mClickListener;

    public RecipeAdapter(Context context, ListItemClickListener clickListener) {
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(holder.getAdapterPosition());
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

    public interface ListItemClickListener {
        void onClick(Recipe recipe);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name_text_view)
        TextView nameTextView;
        @BindView(R.id.serving_text_view)
        TextView servingTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            nameTextView.setText(mRecipes.get(listIndex).getName());
            servingTextView.setText(mContext.getString(R.string.serving_text, mRecipes.get(listIndex).getServings()));
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(mRecipes.get(getAdapterPosition()));
        }
    }
}
