package com.gramasuvidha.app.ui.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.model.Feedback;
import com.gramasuvidha.app.viewmodel.ProjectViewModel;

public class FeedbackActivity extends AppCompatActivity {

    private ProjectViewModel viewModel;
    private String projectId;
    private boolean isKannada;
    private RatingBar ratingBar;
    private EditText etComment;
    private RadioGroup rgFeedbackType;
    private Spinner spinnerIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        projectId = getIntent().getStringExtra("project_id");
        String projectTitle = getIntent().getStringExtra("project_title");
        isKannada = getIntent().getBooleanExtra("is_kannada", false);

        viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isKannada ? "ಪ್ರತಿಕ್ರಿಯೆ" : "Feedback");
        }

        setupViews(projectTitle);
    }

    private void setupViews(String projectTitle) {
        TextView tvProjectTitle = findViewById(R.id.tv_feedback_project_title);
        tvProjectTitle.setText(projectTitle);

        ratingBar = findViewById(R.id.rating_bar);
        etComment = findViewById(R.id.et_comment);
        rgFeedbackType = findViewById(R.id.rg_feedback_type);
        spinnerIssue = findViewById(R.id.spinner_issue_type);

        // Localize labels
        TextView tvRateLabel = findViewById(R.id.tv_rate_label);
        TextView tvCommentLabel = findViewById(R.id.tv_comment_label);
        TextView tvTypeLabel = findViewById(R.id.tv_type_label);
        RadioButton rbRating = findViewById(R.id.rb_rating);
        RadioButton rbIssue = findViewById(R.id.rb_issue);
        Button btnSubmit = findViewById(R.id.btn_submit_feedback);

        if (isKannada) {
            tvRateLabel.setText("ಯೋಜನೆಯನ್ನು ರೇಟ್ ಮಾಡಿ:");
            tvCommentLabel.setText("ಕಾಮೆಂಟ್ (ಐಚ್ಛಿಕ):");
            tvTypeLabel.setText("ಪ್ರತಿಕ್ರಿಯೆ ವಿಧ:");
            rbRating.setText("ರೇಟಿಂಗ್ ನೀಡಿ");
            rbIssue.setText("ಸಮಸ್ಯೆ ವರದಿ ಮಾಡಿ");
            btnSubmit.setText("ಸಲ್ಲಿಸಿ");
            etComment.setHint("ನಿಮ್ಮ ಅಭಿಪ್ರಾಯ ಬರೆಯಿರಿ...");
        } else {
            tvRateLabel.setText("Rate this Project:");
            tvCommentLabel.setText("Comment (Optional):");
            tvTypeLabel.setText("Feedback Type:");
            rbRating.setText("Rate the Project");
            rbIssue.setText("Report an Issue");
            btnSubmit.setText("Submit Feedback");
            etComment.setHint("Write your feedback here...");
        }

        // Issue spinner
        String[] issues = isKannada
                ? new String[]{"ವಿಧ ಆಯ್ಕೆಮಾಡಿ", "ವಿಳಂಬ", "ಕಳಪೆ ಕಾರ್ಯ", "ಭ್ರಷ್ಟಾಚಾರ", "ಅಪಾಯ", "ಇತರ"}
                : new String[]{"Select Issue Type", "Delay", "Poor Workmanship", "Corruption", "Safety Hazard", "Other"};
        ArrayAdapter<String> issueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, issues);
        issueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssue.setAdapter(issueAdapter);

        // Show/hide issue spinner based on type
        rgFeedbackType.setOnCheckedChangeListener((group, checkedId) -> {
            spinnerIssue.setVisibility(checkedId == R.id.rb_issue ? View.VISIBLE : View.GONE);
        });

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        float rating = ratingBar.getRating();
        String comment = etComment.getText().toString().trim();
        boolean isIssue = rgFeedbackType.getCheckedRadioButtonId() == R.id.rb_issue;
        String issueType = isIssue ? spinnerIssue.getSelectedItem().toString() : "";

        if (!isIssue && rating == 0) {
            Toast.makeText(this, isKannada ? "ದಯವಿಟ್ಟು ರೇಟಿಂಗ್ ನೀಡಿ" : "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        Feedback feedback = new Feedback(projectId, (int) rating, comment, issueType, isIssue);
        viewModel.submitFeedback(feedback);

        Toast.makeText(this,
                isKannada ? "ಪ್ರತಿಕ್ರಿಯೆ ಸಲ್ಲಿಸಲಾಗಿದೆ! ಧನ್ಯವಾದಗಳು" : "Feedback submitted! Thank you",
                Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
