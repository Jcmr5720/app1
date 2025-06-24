package com.example.holamundo.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

/**
 * Entidad que representa una subtarea perteneciente a una {@link Task}.
 */
@Entity(foreignKeys = @ForeignKey(entity = Task.class,
        parentColumns = "id",
        childColumns = "taskId",
        onDelete = ForeignKey.CASCADE),
        tableName = "subtasks")
public class SubTask {
    @PrimaryKey(autoGenerate = true)
    public long id;

    /** ID de la tarea padre. */
    public long taskId;

    /** Título de la subtarea. */
    public String title;

    /** Indica si está completada. */
    public boolean completed;
}
