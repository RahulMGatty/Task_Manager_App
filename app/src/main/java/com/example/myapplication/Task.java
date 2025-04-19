package com.example.myapplication;

public class Task {
    private String title;
    private String description;

    // Required empty constructor for Firestore
    public Task() {}

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
}
