package com.gramasuvidha.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.gramasuvidha.app.R;
import com.gramasuvidha.app.adapter.ProjectAdapter;
import com.gramasuvidha.app.model.Project;
import com.gramasuvidha.app.model.User;
import com.gramasuvidha.app.repository.AuthRepository;
import com.gramasuvidha.app.ui.auth.LoginActivity;
import com.gramasuvidha.app.ui.detail.ProjectDetailActivity;
import com.gramasuvidha.app.viewmodel.ProjectViewModel;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProjectAdapter.OnProjectClickListener {

    private ProjectViewModel viewModel;
    private ProjectAdapter adapter;
    private AuthRepository authRepo;
    private List<Project> allProjectsList;
    private String currentFilter = "All";
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepo = new AuthRepository(this);
        if (!authRepo.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        setupUserGreeting();
        setupRecyclerView();
        setupSearch();
        setupFilterChips();
        setupLanguageToggle();
        setupLogout();
        observeData();
    }

    private void setupUserGreeting() {
        User user = authRepo.getLoggedInUser();
        TextView tvGreeting = findViewById(R.id.tv_user_greeting);
        TextView tvVillage = findViewById(R.id.tv_user_village);
        if (user != null) {
            if (tvGreeting != null) tvGreeting.setText("Hello, " + user.getName().split(" ")[0] + " 👋");
            if (tvVillage != null) tvVillage.setText("📍 " + user.getVillage());
        }
    }

    private void setupLogout() {
        TextView tvLogout = findViewById(R.id.tv_logout);
        if (tvLogout != null) {
            tvLogout.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", (d, w) -> {
                        authRepo.logout();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show());
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_projects);
        adapter = new ProjectAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilterChips() {
        ChipGroup chipGroup = findViewById(R.id.chip_group_filter);
        String[] categories = {"All", "Road Construction", "Water Supply", "Public Facility", "Drainage", "Lake/Pond Rejuvenation"};
        for (String cat : categories) {
            Chip chip = new Chip(this);
            chip.setText(cat);
            chip.setCheckable(true);
            chip.setChecked(cat.equals("All"));
            chip.setOnClickListener(v -> { currentFilter = cat; applyFilters(); });
            chipGroup.addView(chip);
        }
    }

    private void setupLanguageToggle() {
        TextView tvLangToggle = findViewById(R.id.tv_lang_toggle);
        tvLangToggle.setOnClickListener(v -> { viewModel.toggleLanguage(); updateLanguageButton(); });
        updateLanguageButton();
    }

    private void updateLanguageButton() {
        TextView tvLangToggle = findViewById(R.id.tv_lang_toggle);
        boolean isKn = viewModel.isKannada();
        tvLangToggle.setText(isKn ? "EN" : "ಕನ್ನಡ");
        adapter.setKannada(isKn);
        TextView tvHeader = findViewById(R.id.tv_header_title);
        TextView tvSubtitle = findViewById(R.id.tv_header_subtitle);
        if (tvHeader != null) tvHeader.setText(isKn ? "ಗ್ರಾಮ ಸುವಿಧಾ" : "Grama Suvidha");
        if (tvSubtitle != null) tvSubtitle.setText(isKn ? "ಡಿಜಿಟಲ್ ಗ್ರಾಮ ಅಭಿವೃದ್ಧಿ ಪೋರ್ಟಲ್" : "Digital Village Development Portal");
    }

    private void observeData() {
        viewModel.getAllProjects().observe(this, projects -> {
            allProjectsList = projects;
            applyFilters();
            updateStats(projects);
        });
    }

    private void applyFilters() {
        if (allProjectsList == null) return;
        List<Project> filtered = viewModel.filterProjects(allProjectsList, currentFilter, currentQuery, viewModel.isKannada());
        adapter.setProjects(filtered);
        TextView tvEmpty = findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateStats(List<Project> projects) {
        if (projects == null) return;
        int total = projects.size(), completed = 0, inProgress = 0;
        for (Project p : projects) {
            if ("Completed".equals(p.getStatus())) completed++;
            else if ("In Progress".equals(p.getStatus())) inProgress++;
        }
        TextView t = findViewById(R.id.tv_stat_total);
        TextView c = findViewById(R.id.tv_stat_completed);
        TextView i = findViewById(R.id.tv_stat_inprogress);
        if (t != null) t.setText(String.valueOf(total));
        if (c != null) c.setText(String.valueOf(completed));
        if (i != null) i.setText(String.valueOf(inProgress));
    }

    @Override
    public void onProjectClick(Project project) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("project_id", project.getId());
        intent.putExtra("is_kannada", viewModel.isKannada());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshProjects();
    }
}
