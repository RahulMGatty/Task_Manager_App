package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonUpdate, buttonDelete;
    private FirebaseFirestore db;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = findViewById(R.id.editTextEditTitle);
        editTextDescription = findViewById(R.id.editTextEditDescription);
        buttonUpdate = findViewById(R.id.buttonUpdateTask);
        buttonDelete = findViewById(R.id.buttonDeleteTask);

        db = FirebaseFirestore.getInstance();

        // Get passed data
        taskId = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        if (taskId == null || title == null || description == null) {
            Toast.makeText(this, "Invalid task data", Toast.LENGTH_SHORT).show();
            finish();
        }

        editTextTitle.setText(title);
        editTextDescription.setText(description);

        buttonUpdate.setOnClickListener(v -> updateTask());
        buttonDelete.setOnClickListener(v -> deleteTask());
    }

    private void updateTask() {
        String newTitle = editTextTitle.getText().toString().trim();
        String newDescription = editTextDescription.getText().toString().trim();

        if (newTitle.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("tasks").document(taskId)
                .update("title", newTitle, "description", newDescription)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteTask() {
        db.collection("tasks").document(taskId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
