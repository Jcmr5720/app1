package com.example.holamundo.trello;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repositorio que maneja operaciones de tablero en hilos de fondo.
 */
public class BoardRepository {
    private final BoardDao boardDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BoardRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        boardDao = db.boardDao();
    }

    public interface BoardsCallback {
        void onResult(List<Board> boards);
    }

    public void getBoards(BoardsCallback callback) {
        executor.execute(() -> {
            List<Board> list = boardDao.getAll();
            if (callback != null) callback.onResult(list);
        });
    }

    public void insert(Board board, Runnable finished) {
        executor.execute(() -> {
            boardDao.insert(board);
            if (finished != null) finished.run();
        });
    }

    public void update(Board board) {
        executor.execute(() -> boardDao.update(board));
    }

    public void delete(Board board) {
        executor.execute(() -> boardDao.delete(board));
    }
}
