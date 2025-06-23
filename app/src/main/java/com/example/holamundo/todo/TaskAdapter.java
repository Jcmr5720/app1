package com.example.holamundo.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para mostrar las tareas en un RecyclerView.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks = new ArrayList<>();
    private TaskActionsListener listener;

    interface TaskActionsListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onToggle(Task task, boolean completed);
    }

    public void setListener(TaskActionsListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> list) {
        tasks.clear();
        if (list != null) tasks.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        final CheckBox checkCompleted;
        final TextView textTitle;
        final ImageButton buttonEdit;
        final ImageButton buttonDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkCompleted = itemView.findViewById(R.id.check_completed);
            textTitle = itemView.findViewById(R.id.text_title);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }

        void bind(Task task) {
            checkCompleted.setOnCheckedChangeListener(null);
            checkCompleted.setChecked(task.completed);
            textTitle.setText(task.title);
            checkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) listener.onToggle(task, isChecked);
            });
            buttonEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(task);
            });
            buttonDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(task);
            });
        }
    }
}
