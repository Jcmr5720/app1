package com.example.holamundo.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad de Room que representa una tarea.
 */
@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;

    /** Descripción detallada de la tarea. */
    public String description;

    /** Marca temporal de creación en milisegundos. */
    public long createdAt;

    /** Indica si la tarea está completada. */
    public boolean completed;
}
