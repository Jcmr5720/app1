package com.example.holamundo.todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.holamundo.trello.Board;
import com.example.holamundo.trello.Lista;
import com.example.holamundo.trello.Tarjeta;

/**
 * Base de datos Room que almacena las tareas.
 */
@Database(entities = {Task.class, SubTask.class, Board.class, Lista.class, Tarjeta.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract SubTaskDao subTaskDao();
    // Nuevos DAOs para la funcionalidad estilo Trello
    public abstract com.example.holamundo.trello.BoardDao boardDao();
    public abstract com.example.holamundo.trello.ListaDao listaDao();
    public abstract com.example.holamundo.trello.TarjetaDao tarjetaDao();

    private static volatile AppDatabase INSTANCE;

    /**
     * Obtiene la instancia singleton de la base de datos.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "tasks.db")
                            // El esquema es sencillo, en caso de cambio se recrea la BD
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
