package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTaskTitle, editTextTaskDescription;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTaskTitle = findViewById(R.id.editTextTitle);
        editTextTaskDescription = findViewById(R.id.editTextDescription);
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.buttonSaveTask).setOnClickListener(v -> {
            String taskTitle = editTextTaskTitle.getText().toString().trim();
            String taskDescription = editTextTaskDescription.getText().toString().trim();

            // TEMPORARY: Debug Toast to check task data before saving
            Toast.makeText(AddTaskActivity.this, "Saving Task: " + taskTitle + " - " + taskDescription, Toast.LENGTH_SHORT).show();

            if (!taskTitle.isEmpty() && !taskDescription.isEmpty()) {
                saveTaskToFirestore(taskTitle, taskDescription);
            } else {
                Toast.makeText(AddTaskActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTaskToFirestore(String title, String description) {
        // Creating a new task object
        Task newTask = new Task(title, description);

        // Saving the task to Firestore
        db.collection("tasks")
                .add(newTask)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to HomeActivity after task is added
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddTaskActivity.this, "Error saving task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
