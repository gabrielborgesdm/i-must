package com.gabriel.taskapp.service.listeners;

import com.gabriel.taskapp.service.models.local.LocalTaskModel;

public interface CompletedTaskListener {
    void onDelete(LocalTaskModel todo);
}
