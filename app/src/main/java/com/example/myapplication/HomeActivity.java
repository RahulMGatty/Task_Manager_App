package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        // Set up custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(adapter);

        // Initialize FloatingActionButton and set click listener
        fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddTaskActivity.class));
        });

        // Initialize Firestore and load tasks
        db = FirebaseFirestore.getInstance();
        loadTasksFromFirestore(null);
    }

    private void loadTasksFromFirestore(Boolean completedFilter) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users")
                    .document(user.getUid())
                    .collection("tasks")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        taskList.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Task task = doc.toObject(Task.class);
                            if (task != null) {
                                task.setId(doc.getId());
                                if (completedFilter == null || task.isCompleted() == completedFilter) {
                                    taskList.add(task);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(HomeActivity.this, "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void sortTasksAlphabetically(boolean ascending) {
        if (ascending) {
            taskList.sort((t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle()));
        } else {
            taskList.sort((t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle()));
        }
        adapter.notifyDataSetChanged();
    }

    private void sortTasksByTime(boolean newestFirst) {
        if (newestFirst) {
            taskList.sort((t1, t2) -> Long.compare(t2.getTimestamp(), t1.getTimestamp()));
        } else {
            taskList.sort((t1, t2) -> Long.compare(t1.getTimestamp(), t2.getTimestamp()));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromFirestore(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showFilterSortDialog() {
        String[] options = {"All", "Completed", "Pending", "A–Z", "Z–A", "Newest First", "Oldest First"};
        new AlertDialog.Builder(this)
                .setTitle("Filter / Sort Tasks")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            loadTasksFromFirestore(null);
                            break;
                        case 1:
                            loadTasksFromFirestore(true);
                            break;
                        case 2:
                            loadTasksFromFirestore(false);
                            break;
                        case 3:
                            sortTasksAlphabetically(true);
                            break;
                        case 4:
                            sortTasksAlphabetically(false);
                            break;
                        case 5:
                            sortTasksByTime(true);  // newest first
                            break;
                        case 6:
                            sortTasksByTime(false); // oldest first
                            break;
                    }
                })
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_filter) {
            showFilterSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
