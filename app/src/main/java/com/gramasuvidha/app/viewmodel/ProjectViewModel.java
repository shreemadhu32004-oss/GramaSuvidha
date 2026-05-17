package com.gramasuvidha.app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.gramasuvidha.app.model.Feedback;
import com.gramasuvidha.app.model.Project;
import com.gramasuvidha.app.repository.ProjectRepository;
import java.util.ArrayList;
import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    private final ProjectRepository repository;
    private MutableLiveData<List<Project>> allProjects;
    private MutableLiveData<String> filterCategory = new MutableLiveData<>("All");
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<String> language;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repository = new ProjectRepository(application);
        language = new MutableLiveData<>(repository.getLanguage());
    }

    public LiveData<List<Project>> getAllProjects() {
        if (allProjects == null) {
            allProjects = repository.getAllProjects();
        }
        return allProjects;
    }

    public void refreshProjects() {
        MutableLiveData<List<Project>> fresh = repository.getAllProjects();
        fresh.observeForever(projects -> {
            if (allProjects != null) allProjects.postValue(projects);
        });
    }

    public LiveData<Project> getProjectById(String id) {
        return repository.getProjectById(id);
    }

    public LiveData<String> getLanguage() {
        return language;
    }

    public void toggleLanguage() {
        String current = language.getValue();
        String next = "en".equals(current) ? "kn" : "en";
        repository.setLanguage(next);
        language.setValue(next);
    }

    public boolean isKannada() {
        return "kn".equals(language.getValue());
    }

    public void submitFeedback(Feedback feedback) {
        repository.saveFeedback(feedback);
        refreshProjects();
    }

    public List<Feedback> getFeedbackForProject(String projectId) {
        return repository.getFeedbackForProject(projectId);
    }

    public void setFilter(String category) {
        filterCategory.setValue(category);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public List<Project> filterProjects(List<Project> projects, String category, String query, boolean isKannada) {
        if (projects == null) return new ArrayList<>();
        List<Project> result = new ArrayList<>();
        for (Project p : projects) {
            boolean matchCat = "All".equals(category) || p.getCategory().equals(category);
            boolean matchQuery = query == null || query.isEmpty()
                    || p.getTitle().toLowerCase().contains(query.toLowerCase())
                    || p.getLocation().toLowerCase().contains(query.toLowerCase())
                    || (isKannada && p.getTitleKn() != null && p.getTitleKn().contains(query));
            if (matchCat && matchQuery) result.add(p);
        }
        return result;
    }
}
