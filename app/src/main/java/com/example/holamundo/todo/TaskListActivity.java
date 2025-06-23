package com.example.holamundo.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.holamundo.R;

import java.util.List;

/**
 * Activity principal que muestra la lista de tareas.
 */
public class TaskListActivity extends AppCompatActivity implements TaskAdapter.TaskActionsListener {

    private TaskAdapter adapter;
    private TaskRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setTitle(R.string.title_tasks);

        repository = new TaskRepository(this);
        adapter = new TaskAdapter();
        adapter.setListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        repository.getTasks(tasks -> runOnUiThread(() -> adapter.setTasks(tasks)));
    }

    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate a fresh instance of the dialog layout each time to avoid
        // reusing a view that already has a parent.
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        EditText editText = dialogView.findViewById(R.id.edit_task);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_task)
                // Pass the whole inflated layout to the dialog so that the EditText
                // does not already have a parent when added.
                .setView(dialogView)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String text = editText.getText().toString().trim();
                    if (!text.isEmpty()) {
                        Task task = new Task();
                        task.title = text;
                        repository.insert(task, this::loadTasks);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showEditDialog(Task task) {
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate a new view for the dialog to avoid keeping references to
        // a view with an existing parent.
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        EditText editText = dialogView.findViewById(R.id.edit_task);
        editText.setText(task.title);

        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_task)
                // Use the entire layout as the custom dialog view.
                .setView(dialogView)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String text = editText.getText().toString().trim();
                    if (!text.isEmpty()) {
                        task.title = text;
                        repository.update(task);
                        loadTasks();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onEdit(Task task) {
        showEditDialog(task);
    }

    @Override
    public void onDelete(Task task) {
        repository.delete(task);
        loadTasks();
        Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggle(Task task, boolean completed) {
        task.completed = completed;
        repository.update(task);
    }
}
