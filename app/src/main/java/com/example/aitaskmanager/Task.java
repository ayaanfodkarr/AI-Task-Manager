package com.example.aitaskmanager;

public class Task {

    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Status { PENDING, IN_PROGRESS, DONE }

    private String id;
    private String title;
    private String description;
    private String aiAnalysis;
    private Priority priority;
    private Status status;
    private long createdAt;

    public Task(String title, String description) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.description = description;
        this.priority = Priority.MEDIUM;
        this.status = Status.PENDING;
        this.createdAt = System.currentTimeMillis();
        this.aiAnalysis = "";
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAiAnalysis() { return aiAnalysis; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public long getCreatedAt() { return createdAt; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setAiAnalysis(String aiAnalysis) { this.aiAnalysis = aiAnalysis; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(Status status) { this.status = status; }

    public String getPriorityLabel() {
        switch (priority) {
            case HIGH:   return "HIGH";
            case LOW:    return "LOW";
            default:     return "MED";
        }
    }

    public String getStatusLabel() {
        switch (status) {
            case IN_PROGRESS: return "IN PROGRESS";
            case DONE:        return "DONE";
            default:          return "PENDING";
        }
    }
}
