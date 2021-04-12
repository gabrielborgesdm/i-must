package com.gabriel.todoapp.service.listener;

import com.gabriel.todoapp.service.model.remote.SignUpModel;

public interface SignUpListener {
    void onSuccess(SignUpModel model);
    void onFailure(String message);
}
