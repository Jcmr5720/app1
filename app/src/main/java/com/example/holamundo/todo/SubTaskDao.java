package com.example.holamundo.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Acceso a datos para subtareas. */
@Dao
public interface SubTaskDao {
    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    List<SubTask> getForTask(long taskId);

    @Insert
    long insert(SubTask subTask);

    @Update
    void update(SubTask subTask);

    @Delete
    void delete(SubTask subTask);
}
