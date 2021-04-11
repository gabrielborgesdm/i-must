package com.example.todoapp.service.listener;

import com.example.todoapp.service.HeaderModel;

public interface ApiListener {
    void onSuccess(HeaderModel model);
    void onFailure(String message);
}
