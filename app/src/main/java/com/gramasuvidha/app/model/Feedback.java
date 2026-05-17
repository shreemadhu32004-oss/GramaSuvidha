package com.gramasuvidha.app.model;

public class Feedback {
    private String projectId;
    private int rating;
    private String comment;
    private String issueType;
    private String timestamp;
    private boolean isIssueReport;

    public Feedback() {}

    public Feedback(String projectId, int rating, String comment, String issueType, boolean isIssueReport) {
        this.projectId = projectId;
        this.rating = rating;
        this.comment = comment;
        this.issueType = issueType;
        this.isIssueReport = isIssueReport;
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public String getProjectId() { return projectId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getIssueType() { return issueType; }
    public String getTimestamp() { return timestamp; }
    public boolean isIssueReport() { return isIssueReport; }

    public void setProjectId(String projectId) { this.projectId = projectId; }
    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setIssueType(String issueType) { this.issueType = issueType; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setIssueReport(boolean issueReport) { isIssueReport = issueReport; }
}
