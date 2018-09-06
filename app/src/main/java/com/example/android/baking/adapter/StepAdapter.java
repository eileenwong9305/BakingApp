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
import com.example.android.baking.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private Context context;
    private List<Step> steps;
    private StepItemClickListener listener;

    public interface StepItemClickListener {
        void onClick(Step step);
    }

    public StepAdapter(Context context, StepItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.detail_step_num_tv)
        TextView stepNumTextView;
        @BindView(R.id.detail_short_desc_tv)
        TextView shortDescTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int itemIndex) {
            Step step = steps.get(itemIndex);
            stepNumTextView.setText(String.valueOf(step.getStep()+1));
            shortDescTextView.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View view) {
            listener.onClick(steps.get(getAdapterPosition()));
        }
    }
}
