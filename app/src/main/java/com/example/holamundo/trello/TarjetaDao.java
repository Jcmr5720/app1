package com.example.holamundo.trello;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** DAO para tarjetas. */
@Dao
public interface TarjetaDao {
    @Query("SELECT * FROM tarjetas WHERE listaId = :listaId ORDER BY posicion")
    List<Tarjeta> getForLista(long listaId);

    @Insert
    long insert(Tarjeta tarjeta);

    @Update
    void update(Tarjeta tarjeta);

    @Delete
    void delete(Tarjeta tarjeta);
}
