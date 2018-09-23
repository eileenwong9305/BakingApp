package com.example.android.baking.ui.detail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.example.android.baking.databinding.ListStepItemBinding;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> mSteps;
    private StepItemClickListener mClickListener;

    public StepAdapter(StepItemClickListener listener, List<Step> steps) {
        mClickListener = listener;
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListStepItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_step_item, parent, false);
        return new StepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {
        holder.binding.setStep(mSteps.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
        notifyDataSetChanged();
    }

    public interface StepItemClickListener {
        void onClick(int position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        final ListStepItemBinding binding;

        public StepViewHolder(ListStepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
