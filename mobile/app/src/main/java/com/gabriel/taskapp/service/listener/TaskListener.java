package com.gabriel.taskapp.service.listener;

import com.gabriel.taskapp.service.model.local.TaskModel;

public interface TaskListener {
    public void onEdit(TaskModel todo);
    public void onDelete(TaskModel todo);
    public void onComplete(TaskModel todo);
}
