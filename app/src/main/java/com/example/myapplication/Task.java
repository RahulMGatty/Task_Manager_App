package com.example.myapplication;

import com.google.firebase.Timestamp;

public class Task {
    private String id;
    private String title;
    private String description;
    private boolean completed;
    private Timestamp timestamp;

    // Firestore requires empty constructor
    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Optional: use this to get timestamp as milliseconds
    public long getTimestampMillis() {
        return timestamp != null ? timestamp.toDate().getTime() : 0;
    }
}
