package com.example.holamundo.trello;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa un tablero de tareas.
 */
@Entity(tableName = "boards")
public class Board {
    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Nombre del tablero. */
    public String nombre;
}
