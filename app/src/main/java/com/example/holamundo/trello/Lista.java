package com.example.holamundo.trello;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa una lista dentro de un tablero.
 */
@Entity(foreignKeys = @ForeignKey(entity = Board.class,
        parentColumns = "id",
        childColumns = "boardId",
        onDelete = ForeignKey.CASCADE),
        tableName = "listas")
public class Lista {
    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Id del tablero al que pertenece. */
    public long boardId;

    /** Nombre de la lista. */
    public String nombre;

    /** Posición para ordenar. */
    public int posicion;
}
