package com.example.todoapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.service.model.TodoModel;
import com.example.todoapp.view.listener.TodoListener;
import com.example.todoapp.view.viewholder.TodoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private List<TodoModel> mTodoList = new ArrayList();
    private TodoListener mListener;

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
        return mTodoList.size();
    }

    public void updateTodo(List<TodoModel> list){
        mTodoList = list;
        notifyDataSetChanged();
    }

    public void attachListener(TodoListener listener){
        mListener = listener;
    }
}
