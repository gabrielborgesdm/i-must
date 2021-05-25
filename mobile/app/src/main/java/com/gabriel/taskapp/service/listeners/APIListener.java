package com.gabriel.taskapp.service.listeners;

public interface APIListener <T> {
    void onSuccess(T model);
    void onFailure(String message);
}
