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

    public boolean completed;
}
