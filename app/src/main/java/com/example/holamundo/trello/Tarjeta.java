package com.example.holamundo.trello;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa una tarjeta dentro de una lista.
 */
@Entity(foreignKeys = @ForeignKey(entity = Lista.class,
        parentColumns = "id",
        childColumns = "listaId",
        onDelete = ForeignKey.CASCADE),
        tableName = "tarjetas")
public class Tarjeta {
    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Id de la lista a la que pertenece. */
    public long listaId;

    /** Título de la tarjeta. */
    public String titulo;

    /** Descripción. */
    public String descripcion;

    /** Fecha límite en milisegundos. */
    public Long fechaLimite;

    /** Indica si está completada. */
    public boolean completada;

    /** Posición para ordenar. */
    public int posicion;
}
