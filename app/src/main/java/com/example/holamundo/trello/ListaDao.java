package com.example.holamundo.trello;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** DAO para listas. */
@Dao
public interface ListaDao {
    @Query("SELECT * FROM listas WHERE boardId = :boardId ORDER BY posicion")
    List<Lista> getForBoard(long boardId);

    @Insert
    long insert(Lista lista);

    @Update
    void update(Lista lista);

    @Delete
    void delete(Lista lista);
}
