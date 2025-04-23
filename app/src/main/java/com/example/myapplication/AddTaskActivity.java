package com.example.myapplication;

import android.os.Bundle;
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

            if (!taskTitle.isEmpty() && !taskDescription.isEmpty()) {
                // Save task to Firestore
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
                .add(newTask) // Add task to "tasks" collection
                .addOnSuccessListener(documentReference -> {
                    // Task saved successfully, navigate back to HomeActivity
                    finish(); // Close AddTaskActivity and return to HomeActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddTaskActivity.this, "Error saving task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
