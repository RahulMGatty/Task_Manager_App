package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonSaveTask;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        db = FirebaseFirestore.getInstance();

        buttonSaveTask.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);

        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
                });
    }
}
