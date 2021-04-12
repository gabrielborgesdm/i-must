package com.gabriel.todoapp.service.listener;

import com.gabriel.todoapp.service.model.remote.HeaderModel;

public interface HeaderListener {
    void onSuccess(HeaderModel model);
    void onFailure(String message);
}
