package com.example.holamundo.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.holamundo.R;

/**
 * Activity principal que muestra la lista de tareas.
 */
public class TaskListActivity extends AppCompatActivity implements TaskAdapter.TaskActionsListener {

    private TaskAdapter adapter;
    private TaskRepository repository;
    private String currentFilter = "ALL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setTitle(R.string.title_tasks);

        repository = new TaskRepository(this);
        adapter = new TaskAdapter();
        adapter.setListener(this);

        Spinner spinner = findViewById(R.id.spinner_filter);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.filters, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] values = getResources().getStringArray(R.array.filters_values);
                currentFilter = values[position];
                loadTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner spinnerCategoryFilter = findViewById(R.id.spinner_category_filter);
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryFilter.setAdapter(catAdapter);
        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Manejar deslizamientos para completar o eliminar tareas
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                Task task = adapter.getTask(vh.getAdapterPosition());
                if (direction == ItemTouchHelper.RIGHT) {
                    task.completed = !task.completed;
                    repository.update(task);
                    adapter.notifyItemChanged(vh.getAdapterPosition());
                } else {
                    onDelete(task);
                }
            }
        });
        helper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        Spinner catSpinner = findViewById(R.id.spinner_category_filter);
        String selectedCat = (String) catSpinner.getSelectedItem();
        TaskRepository.TasksCallback callback = tasks -> {
            if (selectedCat != null && !selectedCat.isEmpty()) {
                if (!selectedCat.equalsIgnoreCase("All")) {
                    java.util.List<Task> filtered = new java.util.ArrayList<>();
                    for (Task t : tasks) {
                        if (selectedCat.equals(t.category)) filtered.add(t);
                    }
                    tasks = filtered;
                }
            }
            java.util.List<Task> finalList = tasks;
            runOnUiThread(() -> adapter.setTasks(finalList));
        };
        switch (currentFilter) {
            case "DONE":
                repository.getCompleted(callback);
                break;
            case "PENDING":
                repository.getPending(callback);
                break;
            default:
                repository.getTasks(callback);
        }
    }

    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate a fresh instance of the dialog layout each time to avoid
        // reusing a view that already has a parent.
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        EditText editTitle = dialogView.findViewById(R.id.edit_title);
        EditText editDesc = dialogView.findViewById(R.id.edit_description);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinner_priority);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        EditText editDue = dialogView.findViewById(R.id.edit_due_date);

        ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(pAdapter);

        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(cAdapter);

        new AlertDialog.Builder(this)
                .setTitle(R.string.add_task)
                // Pass the whole inflated layout to the dialog so that the EditText
                // does not already have a parent when added.
                .setView(dialogView)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String title = editTitle.getText().toString().trim();
                    String desc = editDesc.getText().toString().trim();
                    if (title.isEmpty()) {
                        editTitle.setError(getString(R.string.title_hint));
                    } else {
                        Task task = new Task();
                        task.title = title;
                        task.description = desc;
                        task.priority = spinnerPriority.getSelectedItemPosition();
                        task.category = spinnerCategory.getSelectedItem().toString();
                        String dueText = editDue.getText().toString().trim();
                        if (!dueText.isEmpty()) {
                            try {
                                java.text.DateFormat df = java.text.DateFormat.getDateInstance();
                                task.dueDate = df.parse(dueText).getTime();
                            } catch (Exception ignore) {}
                        }
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
        EditText editTitle = dialogView.findViewById(R.id.edit_title);
        EditText editDesc = dialogView.findViewById(R.id.edit_description);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinner_priority);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        EditText editDue = dialogView.findViewById(R.id.edit_due_date);

        ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorities, android.R.layout.simple_spinner_item);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(pAdapter);
        spinnerPriority.setSelection(task.priority);

        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(cAdapter);
        int cIndex = java.util.Arrays.asList(getResources().getStringArray(R.array.categories)).indexOf(task.category);
        if (cIndex >= 0) spinnerCategory.setSelection(cIndex);
        if (task.dueDate != null && task.dueDate > 0) {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance();
            editDue.setText(df.format(new java.util.Date(task.dueDate)));
        }
        editTitle.setText(task.title);
        editDesc.setText(task.description);

        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_task)
                // Use the entire layout as the custom dialog view.
                .setView(dialogView)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String title = editTitle.getText().toString().trim();
                    String desc = editDesc.getText().toString().trim();
                    if (title.isEmpty()) {
                        editTitle.setError(getString(R.string.title_hint));
                    } else {
                        task.title = title;
                        task.description = desc;
                        task.priority = spinnerPriority.getSelectedItemPosition();
                        task.category = spinnerCategory.getSelectedItem().toString();
                        String dueText = editDue.getText().toString().trim();
                        if (!dueText.isEmpty()) {
                            try {
                                java.text.DateFormat df = java.text.DateFormat.getDateInstance();
                                task.dueDate = df.parse(dueText).getTime();
                            } catch (Exception ignore) {}
                        } else {
                            task.dueDate = null;
                        }
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
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    repository.delete(task);
                    loadTasks();
                    Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onToggle(Task task, boolean completed) {
        task.completed = completed;
        repository.update(task);
    }
}
