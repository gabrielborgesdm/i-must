package com.gabriel.i_must.service.listeners;

import com.gabriel.i_must.service.models.local.LocalTaskModel;

public interface CompletedTaskListener {
    void onDelete(LocalTaskModel todo);
}
