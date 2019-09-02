package com.newtonapp.customer.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newtonapp.customer.R;

import java.util.List;

public class ProblemTrackingAdapter extends RecyclerView.Adapter<ProblemTrackingAdapter.ProblemTrackingViewHolder> {

    private List<String> problems;

    public ProblemTrackingAdapter(List<String> problems) {
        this.problems = problems;
    }

    @NonNull
    @Override
    public ProblemTrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_problem_tracking, parent, false);
        return new ProblemTrackingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemTrackingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (problems == null) return 0;
        else return problems.size();
    }

    class ProblemTrackingViewHolder extends RecyclerView.ViewHolder {

        public ProblemTrackingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
