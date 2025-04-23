package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Task: " + task.getTitle(), Toast.LENGTH_SHORT).show();
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

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewTaskTitle);
            descriptionText = itemView.findViewById(R.id.textViewTaskDescription);
        }
    }
}
