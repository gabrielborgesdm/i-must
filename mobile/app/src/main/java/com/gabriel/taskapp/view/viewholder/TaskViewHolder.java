package com.gabriel.taskapp.view.viewholder;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.listener.TaskListener;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TaskListener mListener;
    public TaskViewHolder(@NonNull View itemView, TaskListener listener) {
        super(itemView);
        mListener = listener;
    }

    public void bind(TaskModel task){
        ImageView buttonEdit = itemView.findViewById(R.id.button_edit_guest);
        ImageView buttonComplete = itemView.findViewById(R.id.button_complete_guest);
        ImageView buttonRemove = itemView.findViewById(R.id.button_remove_guest);
        TextView textName = itemView.findViewById(R.id.text_description);

        textName.setText(task.getDescription());

        if(!task.isCompleted()){

            buttonEdit.setOnClickListener(v -> {
                mListener.onEdit(task);
            });


            buttonComplete.setOnClickListener(v -> {
                mListener.onComplete(task);
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
                        mListener.onDelete(task);
                    })
                    .setNeutralButton(R.string.cancel, null)
                    .show();
        });


    }
}
