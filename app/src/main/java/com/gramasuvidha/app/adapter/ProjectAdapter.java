package com.gramasuvidha.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.model.Project;
import com.gramasuvidha.app.utils.LanguageUtils;
import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }

    private List<Project> projects = new ArrayList<>();
    private OnProjectClickListener listener;
    private boolean isKannada = false;
    private Context context;

    public ProjectAdapter(Context context, OnProjectClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    public void setKannada(boolean kannada) {
        this.isKannada = kannada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project_card, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);

        // Title & Location
        holder.tvTitle.setText(isKannada && project.getTitleKn() != null
                ? project.getTitleKn() : project.getTitle());
        holder.tvLocation.setText(isKannada && project.getLocationKn() != null
                ? project.getLocationKn() : project.getLocation());
        holder.tvCategory.setText(isKannada && project.getCategoryKn() != null
                ? project.getCategoryKn() : project.getCategory());

        // Budget
        holder.tvBudget.setText(LanguageUtils.formatBudget(project.getBudgetAllocated()));

        // Progress
        holder.progressBar.setProgress(project.getProgressPercent());
        holder.tvProgress.setText(project.getProgressPercent() + "% " +
                (isKannada ? "ಪೂರ್ಣ" : "Completed"));

        // Status
        String statusText = isKannada && project.getStatusKn() != null
                ? project.getStatusKn() : project.getStatus();
        holder.tvStatus.setText(statusText);
        holder.tvStatus.setBackgroundColor(LanguageUtils.getStatusColor(project.getStatus()));

        // Rating
        if (project.getTotalRatings() > 0) {
            holder.tvRating.setText(String.format("★ %.1f (%d)", project.getAverageRating(), project.getTotalRatings()));
        } else {
            holder.tvRating.setText(isKannada ? "ರೇಟಿಂಗ್ ಇಲ್ಲ" : "No ratings yet");
        }

        // Timeline
        holder.tvTimeline.setText(project.getStartDate() + " → " + project.getExpectedEndDate());

        // Image
        Glide.with(context)
                .load(project.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_project)
                .error(R.drawable.placeholder_project)
                .centerCrop()
                .into(holder.ivProject);

        // Click
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) listener.onProjectClick(project);
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivProject;
        TextView tvTitle, tvLocation, tvCategory, tvBudget;
        TextView tvProgress, tvStatus, tvRating, tvTimeline;
        ProgressBar progressBar;

        ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_project);
            ivProject = itemView.findViewById(R.id.iv_project_image);
            tvTitle = itemView.findViewById(R.id.tv_project_title);
            tvLocation = itemView.findViewById(R.id.tv_project_location);
            tvCategory = itemView.findViewById(R.id.tv_project_category);
            tvBudget = itemView.findViewById(R.id.tv_budget);
            tvProgress = itemView.findViewById(R.id.tv_progress_text);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvTimeline = itemView.findViewById(R.id.tv_timeline);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
