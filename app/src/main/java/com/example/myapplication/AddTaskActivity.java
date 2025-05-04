package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                saveTaskToFirestore(taskTitle, taskDescription);
            } else {
                Toast.makeText(AddTaskActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTaskToFirestore(String title, String description) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(title, description);
        newTask.setCompleted(false);
        db.collection("users")
                .document(user.getUid())
                .collection("tasks")
                .add(newTask)
                .addOnSuccessListener(documentReference -> finish()) // go back to HomeActivity
                .addOnFailureListener(e ->
                        Toast.makeText(AddTaskActivity.this, "Error saving task: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
