package com.example.todoapp.view.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.service.model.TodoModel;
import com.example.todoapp.view.listener.TodoListener;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    TodoListener mListener;
    public TodoViewHolder(@NonNull View itemView, TodoListener listener) {
        super(itemView);
        mListener = listener;
    }

    public void bind(TodoModel todo){
        TextView textName = itemView.findViewById(R.id.text_description);
        textName.setText(todo.getDescription());

        ImageView buttonEdit = itemView.findViewById(R.id.button_edit_guest);
        buttonEdit.setOnClickListener(v -> {
            mListener.onEdit(todo);
        });
        ImageView buttonRemove = itemView.findViewById(R.id.button_remove_guest);
        buttonRemove.setOnClickListener(v -> {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(R.string.todo_removal)
                    .setMessage(R.string.want_to_remove)
                    .setPositiveButton(R.string.remove, (dialog, which) -> {
                        mListener.onDelete(todo);
                    })
                    .setNeutralButton(R.string.cancel, null)
                    .show();
        });

        ImageView buttonComplete = itemView.findViewById(R.id.button_complete_guest);
        buttonComplete.setOnClickListener(v -> {
            mListener.onComplete(todo);
        });
    }
}
