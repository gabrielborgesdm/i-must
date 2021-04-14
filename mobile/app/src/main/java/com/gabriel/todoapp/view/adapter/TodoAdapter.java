package com.gabriel.todoapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.todoapp.R;
import com.gabriel.todoapp.service.model.local.TaskModel;
import com.gabriel.todoapp.service.listener.TaskListener;
import com.gabriel.todoapp.view.viewholder.TodoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<TaskModel> mTodoList = new ArrayList();
    private TaskListener mListener;

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todo, parent, false);
        return new TodoViewHolder(item, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(mTodoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTodoList == null ? 0 : mTodoList.size();
    }

    public void updateTodo(List<TaskModel> list){
        mTodoList = list;
        notifyDataSetChanged();
    }

    public void attachListener(TaskListener listener){
        mListener = listener;
    }
}
