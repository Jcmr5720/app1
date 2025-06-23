package com.example.holamundo.todo;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repositorio que maneja las operaciones de base de datos en hilos de fondo.
 */
public class TaskRepository {
    private final TaskDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.taskDao();
    }

    public interface TasksCallback {
        void onResult(List<Task> tasks);
    }

    public void getTasks(TasksCallback callback) {
        executor.execute(() -> {
            List<Task> tasks = dao.getAll();
            if (callback != null) {
                callback.onResult(tasks);
            }
        });
    }

    public void insert(Task task, Runnable finished) {
        executor.execute(() -> {
            dao.insert(task);
            if (finished != null) finished.run();
        });
    }

    public void update(Task task) {
        executor.execute(() -> dao.update(task));
    }

    public void delete(Task task) {
        executor.execute(() -> dao.delete(task));
    }
}
