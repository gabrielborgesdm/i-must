package com.gabriel.i_must.service.listeners;

public interface APIListener <T> {
    void onSuccess(T model);
    void onFailure(String message);
}
