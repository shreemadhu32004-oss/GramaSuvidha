package com.gramasuvidha.app.ui.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.model.Project;
import com.gramasuvidha.app.ui.feedback.FeedbackActivity;
import com.gramasuvidha.app.utils.LanguageUtils;
import com.gramasuvidha.app.viewmodel.ProjectViewModel;

public class ProjectDetailActivity extends AppCompatActivity {

    private ProjectViewModel viewModel;
    private String projectId;
    private boolean isKannada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        projectId = getIntent().getStringExtra("project_id");
        isKannada = getIntent().getBooleanExtra("is_kannada", false);

        viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isKannada ? "ಯೋಜನೆ ವಿವರ" : "Project Details");
        }

        loadProject();
    }

    private void loadProject() {
        viewModel.getProjectById(projectId).observe(this, project -> {
            if (project != null) {
                bindProject(project);
            }
        });
    }

    private void bindProject(Project project) {
        // Header image
        ImageView ivHeader = findViewById(R.id.iv_detail_header);
        Glide.with(this)
                .load(project.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivHeader);

        // Titles
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        tvTitle.setText(isKannada && project.getTitleKn() != null ? project.getTitleKn() : project.getTitle());

        TextView tvCategory = findViewById(R.id.tv_detail_category);
        tvCategory.setText(isKannada && project.getCategoryKn() != null ? project.getCategoryKn() : project.getCategory());

        TextView tvLocation = findViewById(R.id.tv_detail_location);
        tvLocation.setText("📍 " + (isKannada && project.getLocationKn() != null ? project.getLocationKn() : project.getLocation()));

        // Status
        TextView tvStatus = findViewById(R.id.tv_detail_status);
        String statusText = isKannada && project.getStatusKn() != null ? project.getStatusKn() : project.getStatus();
        tvStatus.setText(statusText);
        tvStatus.setBackgroundColor(LanguageUtils.getStatusColor(project.getStatus()));

        // Budget
        TextView tvBudgetAllocated = findViewById(R.id.tv_budget_allocated);
        TextView tvBudgetSpent = findViewById(R.id.tv_budget_spent);
        tvBudgetAllocated.setText(LanguageUtils.formatBudget(project.getBudgetAllocated()));
        tvBudgetSpent.setText(LanguageUtils.formatBudget(project.getBudgetSpent()));

        // Progress
        ProgressBar progressBar = findViewById(R.id.progress_detail);
        progressBar.setProgress(project.getProgressPercent());
        TextView tvProgressText = findViewById(R.id.tv_progress_detail);
        tvProgressText.setText(project.getProgressPercent() + "% " + (isKannada ? "ಪೂರ್ಣ" : "Completed"));

        // Timeline
        TextView tvTimeline = findViewById(R.id.tv_detail_timeline);
        tvTimeline.setText(project.getStartDate() + "  →  " + project.getExpectedEndDate());

        // Contractor
        TextView tvContractor = findViewById(R.id.tv_contractor);
        tvContractor.setText(project.getContractorName());

        // Description
        TextView tvDesc = findViewById(R.id.tv_detail_description);
        tvDesc.setText(isKannada && project.getDescriptionKn() != null
                ? project.getDescriptionKn() : project.getDescription());

        // Rating
        TextView tvRating = findViewById(R.id.tv_detail_rating);
        if (project.getTotalRatings() > 0) {
            tvRating.setText(String.format("★ %.1f / 5.0  (%d %s)",
                    project.getAverageRating(), project.getTotalRatings(),
                    isKannada ? "ರೇಟಿಂಗ್‌ಗಳು" : "ratings"));
        } else {
            tvRating.setText(isKannada ? "ಇನ್ನೂ ರೇಟಿಂಗ್ ಇಲ್ಲ" : "No ratings yet");
        }

        // Updates
        TextView tvUpdates = findViewById(R.id.tv_updates);
        if (project.getUpdates() != null && !project.getUpdates().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String update : project.getUpdates()) {
                sb.append("• ").append(update).append("\n");
            }
            tvUpdates.setText(sb.toString().trim());
        } else {
            tvUpdates.setText(isKannada ? "ಇನ್ನೂ ಅಪ್‌ಡೇಟ್‌ಗಳಿಲ್ಲ" : "No updates yet");
        }

        // Before/After images
        ImageView ivBefore = findViewById(R.id.iv_before);
        ImageView ivAfter = findViewById(R.id.iv_after);
        Glide.with(this).load(project.getBeforeImageUrl()).centerCrop().into(ivBefore);
        Glide.with(this).load(project.getAfterImageUrl()).centerCrop().into(ivAfter);

        // Labels
        TextView tvBeforeLabel = findViewById(R.id.tv_before_label);
        TextView tvAfterLabel = findViewById(R.id.tv_after_label);
        tvBeforeLabel.setText(isKannada ? "ಮೊದಲು" : "Before");
        tvAfterLabel.setText(isKannada ? "ನಂತರ" : "After");

        // Feedback button
        Button btnFeedback = findViewById(R.id.btn_feedback);
        btnFeedback.setText(isKannada ? "ಪ್ರತಿಕ್ರಿಯೆ ನೀಡಿ" : "Give Feedback");
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectDetailActivity.this, FeedbackActivity.class);
            intent.putExtra("project_id", projectId);
            intent.putExtra("project_title", isKannada && project.getTitleKn() != null
                    ? project.getTitleKn() : project.getTitle());
            intent.putExtra("is_kannada", isKannada);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
