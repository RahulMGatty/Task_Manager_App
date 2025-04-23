package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList); // Pass context for Toast
        recyclerView.setAdapter(adapter);

        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddTaskActivity.class));
        });

        db = FirebaseFirestore.getInstance();

        loadTasksFromFirestore(); // Initial task load
    }

    private void loadTasksFromFirestore() {
        db.collection("tasks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taskList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Task task = doc.toObject(Task.class);
                        if (task != null && task.getTitle() != null && task.getDescription() != null) {
                            taskList.add(task);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("HomeActivity", "Error fetching tasks", e);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromFirestore(); // Reload tasks when returning from AddTaskActivity
    }
}
