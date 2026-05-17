package com.gramasuvidha.app.model;

import java.io.Serializable;
import java.util.List;

public class Project implements Serializable {

    private String id;
    private String title;
    private String titleKn;
    private String category;
    private String categoryKn;
    private String location;
    private String locationKn;
    private String description;
    private String descriptionKn;
    private double budgetAllocated;
    private double budgetSpent;
    private int progressPercent;
    private String startDate;
    private String expectedEndDate;
    private String status;
    private String statusKn;
    private String contractorName;
    private String imageUrl;
    private String beforeImageUrl;
    private String afterImageUrl;
    private double averageRating;
    private int totalRatings;
    private List<String> updates;

    public Project() {}

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getTitleKn() { return titleKn; }
    public String getCategory() { return category; }
    public String getCategoryKn() { return categoryKn; }
    public String getLocation() { return location; }
    public String getLocationKn() { return locationKn; }
    public String getDescription() { return description; }
    public String getDescriptionKn() { return descriptionKn; }
    public double getBudgetAllocated() { return budgetAllocated; }
    public double getBudgetSpent() { return budgetSpent; }
    public int getProgressPercent() { return progressPercent; }
    public String getStartDate() { return startDate; }
    public String getExpectedEndDate() { return expectedEndDate; }
    public String getStatus() { return status; }
    public String getStatusKn() { return statusKn; }
    public String getContractorName() { return contractorName; }
    public String getImageUrl() { return imageUrl; }
    public String getBeforeImageUrl() { return beforeImageUrl; }
    public String getAfterImageUrl() { return afterImageUrl; }
    public double getAverageRating() { return averageRating; }
    public int getTotalRatings() { return totalRatings; }
    public List<String> getUpdates() { return updates; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setTitleKn(String titleKn) { this.titleKn = titleKn; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
}
