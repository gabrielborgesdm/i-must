package com.gabriel.todoapp.service.listener;

import com.gabriel.todoapp.service.model.remote.HeaderModel;

public interface APIListener <T> {
    void onSuccess(T model);
    void onFailure(String message);
}
