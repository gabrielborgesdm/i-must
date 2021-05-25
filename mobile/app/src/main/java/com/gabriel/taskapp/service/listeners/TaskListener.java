package com.gabriel.taskapp.service.listeners;

import com.gabriel.taskapp.service.models.local.LocalTaskModel;

public interface TaskListener {
    void onEdit(LocalTaskModel todo);
    void onDelete(LocalTaskModel todo);
    void onComplete(LocalTaskModel todo);
}
