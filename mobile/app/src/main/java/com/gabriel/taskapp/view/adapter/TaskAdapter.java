package com.gabriel.taskapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.gabriel.taskapp.service.listeners.TaskListener;
import com.gabriel.taskapp.view.viewholder.TaskViewHolder;

import java.util.ArrayList;
import java.util.List;

public class  TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<LocalTaskModel> mTodoList = new ArrayList();
    private TaskListener mListener;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task, parent, false);
        return new TaskViewHolder(item, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(mTodoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTodoList == null ? 0 : mTodoList.size();
    }

    public void updateTodo(List<LocalTaskModel> list){
        if(list != null){
            mTodoList = list;
            notifyDataSetChanged();
        }

    }

    public void attachListener(TaskListener listener){
        mListener = listener;
    }
}
