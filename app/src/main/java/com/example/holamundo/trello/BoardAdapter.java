package com.example.holamundo.trello;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.holamundo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para mostrar tableros en un RecyclerView.
 */
public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private final List<Board> boards = new ArrayList<>();
    private BoardActionsListener listener;

    interface BoardActionsListener {
        void onEdit(Board board);
        void onDelete(Board board);
        void onOpen(Board board);
    }

    public void setListener(BoardActionsListener listener) {
        this.listener = listener;
    }

    public void setBoards(List<Board> list) {
        List<Board> old = new ArrayList<>(boards);
        boards.clear();
        if (list != null) boards.addAll(list);
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() { return old.size(); }
            @Override public int getNewListSize() { return boards.size(); }
            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return old.get(oldItemPosition).id == boards.get(newItemPosition).id;
            }
            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return old.get(oldItemPosition).nombre.equals(boards.get(newItemPosition).nombre);
            }
        });
        diff.dispatchUpdatesTo(this);
    }

    @Override
    public long getItemId(int position) {
        return boards.get(position).id;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.bind(boards.get(position));
    }

    @Override
    public int getItemCount() { return boards.size(); }

    class BoardViewHolder extends RecyclerView.ViewHolder {
        final TextView textNombre;
        final ImageButton buttonEdit;
        final ImageButton buttonDelete;
        BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.text_nombre);
            buttonEdit = itemView.findViewById(R.id.button_edit_board);
            buttonDelete = itemView.findViewById(R.id.button_delete_board);
        }
        void bind(Board board) {
            textNombre.setText(board.nombre);
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onOpen(board);
            });
            buttonEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(board);
            });
            buttonDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(board);
            });
        }
    }
}
