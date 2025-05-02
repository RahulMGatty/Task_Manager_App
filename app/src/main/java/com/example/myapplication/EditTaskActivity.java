package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTitle, editDescription;
    private Button buttonUpdate;
    private FirebaseFirestore db;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTitle = findViewById(R.id.editTextEditTitle);
        editDescription = findViewById(R.id.editTextEditDescription);
        buttonUpdate = findViewById(R.id.buttonUpdateTask);
        db = FirebaseFirestore.getInstance();

        // Get data from intent
        taskId = getIntent().getStringExtra("taskId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        // Set current values
        editTitle.setText(title);
        editDescription.setText(description);

        buttonUpdate.setOnClickListener(v -> {
            String newTitle = editTitle.getText().toString().trim();
            String newDesc = editDescription.getText().toString().trim();

            if (!newTitle.isEmpty() && !newDesc.isEmpty()) {
                db.collection("tasks").document(taskId)
                        .update("title", newTitle, "description", newDesc)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Both fields required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

