package com.example.holamundo.trello;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.holamundo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity que muestra la lista de tableros.
 */
public class BoardListActivity extends AppCompatActivity implements BoardAdapter.BoardActionsListener {

    private BoardAdapter adapter;
    private BoardRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        setTitle(R.string.title_boards);

        repository = new BoardRepository(this);
        // El adaptador se encarga de habilitar los IDs estables en su constructor
        // para evitar llamar a setHasStableIds una vez registrado.
        adapter = new BoardAdapter();
        adapter.setListener(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_boards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_board);
        fab.setOnClickListener(v -> showAddDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBoards();
    }

    private void loadBoards() {
        repository.getBoards(adapter::setBoards);
    }

    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_board, null);
        EditText editName = view.findViewById(R.id.edit_board_name);
        new AlertDialog.Builder(this)
                .setTitle(R.string.add_board)
                .setView(view)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String name = editName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        Board board = new Board();
                        board.nombre = name;
                        repository.insert(board, this::loadBoards);
                    } else {
                        Toast.makeText(this, R.string.title_hint, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showEditDialog(Board board) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_board, null);
        EditText editName = view.findViewById(R.id.edit_board_name);
        editName.setText(board.nombre);
        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_board)
                .setView(view)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String name = editName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        board.nombre = name;
                        repository.update(board);
                        loadBoards();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onEdit(Board board) {
        showEditDialog(board);
    }

    @Override
    public void onDelete(Board board) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete_board)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    repository.delete(board);
                    loadBoards();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onOpen(Board board) {
        // Aquí se podría abrir una nueva actividad con las listas del tablero.
        Toast.makeText(this, getString(R.string.open_board, board.nombre), Toast.LENGTH_SHORT).show();
    }
}
