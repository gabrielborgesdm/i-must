package com.gabriel.todoapp.view.viewholder;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.todoapp.R;
import com.gabriel.todoapp.service.model.local.TodoModel;
import com.gabriel.todoapp.service.listener.TodoListener;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    TodoListener mListener;
    public TodoViewHolder(@NonNull View itemView, TodoListener listener) {
        super(itemView);
        mListener = listener;
    }

    public void bind(TodoModel todo){
        ImageView buttonEdit = itemView.findViewById(R.id.button_edit_guest);
        ImageView buttonComplete = itemView.findViewById(R.id.button_complete_guest);
        ImageView buttonRemove = itemView.findViewById(R.id.button_remove_guest);
        TextView textName = itemView.findViewById(R.id.text_description);

        textName.setText(todo.getDescription());

        if(!todo.getCompleted()){

            buttonEdit.setOnClickListener(v -> {
                mListener.onEdit(todo);
            });


            buttonComplete.setOnClickListener(v -> {
                mListener.onComplete(todo);
            });
        } else {
            buttonEdit.setVisibility(View.INVISIBLE);
            buttonComplete.setVisibility(View.INVISIBLE);
            textName.setPaintFlags(textName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

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


    }
}
