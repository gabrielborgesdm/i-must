package com.gabriel.taskapp.service.listener;

import com.gabriel.taskapp.service.model.local.LocalTaskModel;

import org.json.JSONException;

public interface TaskListener {
    public void onEdit(LocalTaskModel todo) throws JSONException;
    public void onDelete(LocalTaskModel todo);
    public void onComplete(LocalTaskModel todo);
}
