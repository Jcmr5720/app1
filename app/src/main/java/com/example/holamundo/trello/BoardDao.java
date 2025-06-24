package com.example.holamundo.trello;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO para tableros.
 */
@Dao
public interface BoardDao {
    @Query("SELECT * FROM boards ORDER BY id DESC")
    List<Board> getAll();

    @Insert
    long insert(Board board);

    @Update
    void update(Board board);

    @Delete
    void delete(Board board);
}
