package com.example.myapplication;

public class Task {
    private String title;
    private String description;
    private String id; // Firestore document ID

    public Task() {
        // Needed for Firestore
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
