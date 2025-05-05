package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        if (task != null && task.getTitle() != null) {
            holder.titleText.setText(task.getTitle());
            holder.descriptionText.setText(task.getDescription());
            holder.checkBoxCompleted.setChecked(task.isCompleted());



            // Visual strike-through if completed
            if (task.isCompleted()) {
                holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.descriptionText.setPaintFlags(holder.descriptionText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.titleText.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                holder.descriptionText.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            } else {
                holder.titleText.setPaintFlags(holder.titleText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.descriptionText.setPaintFlags(holder.descriptionText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.titleText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.descriptionText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }


            // Handle checkbox change
            holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("tasks")
                        .document(task.getId())
                        .update("completed", isChecked)
                        .addOnSuccessListener(aVoid -> {
                            // Optional toast or silent update
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to update task", Toast.LENGTH_SHORT).show());
            });

            // Click to edit
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("taskId", task.getId());
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                context.startActivity(intent);
            });

            // Long click to delete
            holder.itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(userId)
                                    .collection("tasks")
                                    .document(task.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                                        taskList.remove(position);
                                        notifyItemRemoved(position);
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Error deleting task", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });

        } else {
            holder.titleText.setText("Unknown Task");
            holder.descriptionText.setText("");
            holder.itemView.setOnClickListener(v ->
                    Toast.makeText(context, "Invalid task", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descriptionText;
        CheckBox checkBoxCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewTaskTitle);
            descriptionText = itemView.findViewById(R.id.textViewTaskDescription);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted); // <- new field
        }
    }
}
