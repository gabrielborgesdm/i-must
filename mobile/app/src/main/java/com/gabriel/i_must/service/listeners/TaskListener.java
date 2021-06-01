package com.gabriel.i_must.service.listeners;

import com.gabriel.i_must.service.models.local.LocalTaskModel;

public interface TaskListener {
    void onEdit(LocalTaskModel todo);
    void onDelete(LocalTaskModel todo);
    void onComplete(LocalTaskModel todo);
}
