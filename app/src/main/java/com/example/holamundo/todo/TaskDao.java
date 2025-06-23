package com.example.holamundo.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Acceso a datos para las tareas.
 */
@Dao
public interface TaskDao {
    // Obtiene todas las tareas ordenadas por fecha de creación.
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> getAll();

    // Obtiene solo las tareas completadas.
    @Query("SELECT * FROM tasks WHERE completed = 1 ORDER BY createdAt DESC")
    List<Task> getCompleted();

    // Obtiene solo las tareas pendientes.
    @Query("SELECT * FROM tasks WHERE completed = 0 ORDER BY createdAt DESC")
    List<Task> getPending();

    @Insert
    long insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
