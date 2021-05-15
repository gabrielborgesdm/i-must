package com.gabriel.taskapp.service.listener;

import com.gabriel.taskapp.service.model.local.TaskModel;

import org.json.JSONException;

public interface TaskListener {
    public void onEdit(TaskModel todo) throws JSONException;
    public void onDelete(TaskModel todo);
    public void onComplete(TaskModel todo);
}
