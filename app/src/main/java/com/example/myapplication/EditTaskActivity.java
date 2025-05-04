package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonUpdateTask;
    private FirebaseFirestore db;

    private String taskId;
    private String initialTitle;
    private String initialDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = findViewById(R.id.editTextEditTitle);
        editTextDescription = findViewById(R.id.editTextEditDescription);
        buttonUpdateTask = findViewById(R.id.buttonUpdateTask);
        db = FirebaseFirestore.getInstance();

        // Get task details from Intent
        taskId = getIntent().getStringExtra("taskId");
        initialTitle = getIntent().getStringExtra("title");
        initialDescription = getIntent().getStringExtra("description");

        editTextTitle.setText(initialTitle);
        editTextDescription.setText(initialDescription);

        buttonUpdateTask.setOnClickListener(v -> updateTask());
    }

    private void updateTask() {
        String updatedTitle = editTextTitle.getText().toString().trim();
        String updatedDescription = editTextDescription.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .document(user.getUid())
                .collection("tasks")
                .document(taskId)
                .update("title", updatedTitle, "description", updatedDescription)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditTaskActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                    finish(); // Return to HomeActivity
                })
                .addOnFailureListener(e ->
                        Toast.makeText(EditTaskActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
