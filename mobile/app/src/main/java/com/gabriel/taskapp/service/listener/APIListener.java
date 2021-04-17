package com.gabriel.taskapp.service.listener;

public interface APIListener <T> {
    void onSuccess(T model);
    void onFailure(String message);
}
