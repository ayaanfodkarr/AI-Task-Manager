package com.example.aitaskmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskActionListener {
        void onDelete(int position);
        void onStatusCycle(int position);
        void onAnalyze(int position);
    }

    private final List<Task> tasks;
    private final OnTaskActionListener listener;

    public TaskAdapter(List<Task> tasks, OnTaskActionListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, position, listener);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvPriority;
        private final TextView tvStatus;
        private final TextView tvAiAnalysis;
        private final ImageButton btnDelete;
        private final ImageButton btnAnalyze;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView     = itemView.findViewById(R.id.cardView);
            tvTitle      = itemView.findViewById(R.id.tvTaskTitle);
            tvDescription = itemView.findViewById(R.id.tvTaskDescription);
            tvPriority   = itemView.findViewById(R.id.tvPriority);
            tvStatus     = itemView.findViewById(R.id.tvStatus);
            tvAiAnalysis = itemView.findViewById(R.id.tvAiAnalysis);
            btnDelete    = itemView.findViewById(R.id.btnDelete);
            btnAnalyze   = itemView.findViewById(R.id.btnAnalyze);
        }

        void bind(Task task, int position, OnTaskActionListener listener) {
            tvTitle.setText(task.getTitle());

            if (task.getDescription().isEmpty()) {
                tvDescription.setVisibility(View.GONE);
            } else {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(task.getDescription());
            }

            // Priority badge color
            tvPriority.setText(task.getPriorityLabel());
            switch (task.getPriority()) {
                case HIGH:
                    tvPriority.setBackgroundColor(Color.parseColor("#E74C3C"));
                    break;
                case LOW:
                    tvPriority.setBackgroundColor(Color.parseColor("#27AE60"));
                    break;
                default:
                    tvPriority.setBackgroundColor(Color.parseColor("#F39C12"));
                    break;
            }

            // Status badge color
            tvStatus.setText(task.getStatusLabel());
            switch (task.getStatus()) {
                case IN_PROGRESS:
                    tvStatus.setBackgroundColor(Color.parseColor("#2980B9"));
                    break;
                case DONE:
                    tvStatus.setBackgroundColor(Color.parseColor("#1ABC9C"));
                    break;
                default:
                    tvStatus.setBackgroundColor(Color.parseColor("#95A5A6"));
                    break;
            }

            // AI analysis section
            String analysis = task.getAiAnalysis();
            if (analysis != null && !analysis.isEmpty()) {
                tvAiAnalysis.setVisibility(View.VISIBLE);
                tvAiAnalysis.setText(analysis);
            } else {
                tvAiAnalysis.setVisibility(View.GONE);
            }

            // Cycle status on card tap
            cardView.setOnClickListener(v -> listener.onStatusCycle(position));

            btnDelete.setOnClickListener(v -> listener.onDelete(position));
            btnAnalyze.setOnClickListener(v -> listener.onAnalyze(position));
        }
    }
}
