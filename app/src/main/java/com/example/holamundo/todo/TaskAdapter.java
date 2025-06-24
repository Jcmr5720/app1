package com.example.holamundo.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.holamundo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para mostrar las tareas en un RecyclerView.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks = new ArrayList<>();
    public TaskAdapter() {
        setHasStableIds(true); // Mejora animaciones al mantener IDs estables
    }
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
        List<Task> old = new ArrayList<>(tasks);
        tasks.clear();
        if (list != null) tasks.addAll(list);
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return old.size(); }
            @Override
            public int getNewListSize() { return tasks.size(); }
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return old.get(oldItemPosition).id == tasks.get(newItemPosition).id;
            }
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Task o = old.get(oldItemPosition);
                Task n = tasks.get(newItemPosition);
                return o.completed == n.completed &&
                        o.title.equals(n.title) &&
                        ((o.description == null && n.description == null) ||
                         (o.description != null && o.description.equals(n.description))) &&
                        ((o.category == null && n.category == null) ||
                         (o.category != null && o.category.equals(n.category))) &&
                        o.priority == n.priority &&
                        ((o.dueDate == null && n.dueDate == null) ||
                         (o.dueDate != null && o.dueDate.equals(n.dueDate)));
            }
        });
        diff.dispatchUpdatesTo(this);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).id;
    }

    /** Devuelve la tarea en la posición indicada. */
    public Task getTask(int position) {
        return tasks.get(position);
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
        final TextView textDescription;
        final TextView textDueDate;
        final TextView textRepeat;
        final TextView textAttachment;
        final TextView textCategory;
        final ImageButton buttonEdit;
        final ImageButton buttonDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkCompleted = itemView.findViewById(R.id.check_completed);
            textTitle = itemView.findViewById(R.id.text_title);
            textDescription = itemView.findViewById(R.id.text_description);
            textDueDate = itemView.findViewById(R.id.text_due_date);
            textRepeat = itemView.findViewById(R.id.text_repeat);
            textAttachment = itemView.findViewById(R.id.text_attachment);
            textCategory = itemView.findViewById(R.id.text_category);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }

        void bind(Task task) {
            checkCompleted.setOnCheckedChangeListener(null);
            checkCompleted.setChecked(task.completed);
            textTitle.setText(task.title);
            textDescription.setText(task.description);
            if (task.dueDate != null && task.dueDate > 0) {
                java.text.DateFormat df = java.text.DateFormat.getDateInstance();
                textDueDate.setText(df.format(new java.util.Date(task.dueDate)));
            } else {
                textDueDate.setText("");
            }
            String[] repeatValues = itemView.getResources().getStringArray(R.array.repeat_options);
            if (task.repeatInterval >= 0 && task.repeatInterval < repeatValues.length) {
                textRepeat.setText(repeatValues[task.repeatInterval]);
            } else {
                textRepeat.setText("-");
            }
            textAttachment.setText(task.attachmentUri == null ? "" : task.attachmentUri);
            textCategory.setText(task.category);
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
