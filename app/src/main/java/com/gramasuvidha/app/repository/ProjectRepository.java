package com.gramasuvidha.app.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gramasuvidha.app.model.Feedback;
import com.gramasuvidha.app.model.Project;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    private static final String PREFS_NAME = "GramaSuvidhaPrefs";
    private static final String KEY_FEEDBACK_PREFIX = "feedback_";
    private static final String KEY_LANGUAGE = "language";

    private final Context context;
    private final SharedPreferences prefs;
    private final Gson gson;

    public ProjectRepository(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public MutableLiveData<List<Project>> getAllProjects() {
        MutableLiveData<List<Project>> liveData = new MutableLiveData<>();
        new Thread(() -> {
            List<Project> projects = loadProjectsFromJson();
            liveData.postValue(projects);
        }).start();
        return liveData;
    }

    public MutableLiveData<Project> getProjectById(String projectId) {
        MutableLiveData<Project> liveData = new MutableLiveData<>();
        new Thread(() -> {
            List<Project> projects = loadProjectsFromJson();
            for (Project p : projects) {
                if (p.getId().equals(projectId)) {
                    liveData.postValue(p);
                    return;
                }
            }
            liveData.postValue(null);
        }).start();
        return liveData;
    }

    private List<Project> loadProjectsFromJson() {
        try {
            InputStream is = context.getAssets().open("projects.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            Type listType = new TypeToken<List<Project>>() {}.getType();
            List<Project> projects = gson.fromJson(jsonObject.get("projects"), listType);

            // Merge saved feedback ratings
            for (Project project : projects) {
                String savedFeedback = prefs.getString(KEY_FEEDBACK_PREFIX + project.getId(), null);
                if (savedFeedback != null) {
                    List<Feedback> feedbacks = gson.fromJson(savedFeedback,
                            new TypeToken<List<Feedback>>() {}.getType());
                    if (feedbacks != null && !feedbacks.isEmpty()) {
                        double total = project.getAverageRating() * project.getTotalRatings();
                        int count = project.getTotalRatings();
                        for (Feedback f : feedbacks) {
                            if (f.getRating() > 0) {
                                total += f.getRating();
                                count++;
                            }
                        }
                        if (count > 0) {
                            project.setAverageRating(total / count);
                            project.setTotalRatings(count);
                        }
                    }
                }
            }
            return projects;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveFeedback(Feedback feedback) {
        String key = KEY_FEEDBACK_PREFIX + feedback.getProjectId();
        String existing = prefs.getString(key, "[]");
        List<Feedback> feedbacks = gson.fromJson(existing,
                new TypeToken<List<Feedback>>() {}.getType());
        if (feedbacks == null) feedbacks = new ArrayList<>();
        feedbacks.add(feedback);
        prefs.edit().putString(key, gson.toJson(feedbacks)).apply();
    }

    public List<Feedback> getFeedbackForProject(String projectId) {
        String key = KEY_FEEDBACK_PREFIX + projectId;
        String json = prefs.getString(key, "[]");
        List<Feedback> feedbacks = gson.fromJson(json,
                new TypeToken<List<Feedback>>() {}.getType());
        return feedbacks != null ? feedbacks : new ArrayList<>();
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    public void setLanguage(String lang) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply();
    }
}
