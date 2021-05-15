package com.gabriel.taskapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.taskapp.R;
import com.gabriel.taskapp.service.model.local.TaskModel;
import com.gabriel.taskapp.service.listener.TaskListener;
import com.gabriel.taskapp.view.viewholder.TaskViewHolder;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class  TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<TaskModel> mTodoList = new ArrayList();
    private TaskListener mListener;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task, parent, false);
        return new TaskViewHolder(item, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        try {
            holder.bind(mTodoList.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mTodoList == null ? 0 : mTodoList.size();
    }

    public void updateTodo(List<TaskModel> list){
        if(list != null){
            mTodoList = list;
            notifyDataSetChanged();
        }

    }

    public void attachListener(TaskListener listener){
        mListener = listener;
    }
}
