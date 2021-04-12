package com.gabriel.todoapp.service.listener;

import com.gabriel.todoapp.service.model.local.TodoModel;

public interface TodoListener {
    public void onEdit(TodoModel todo);
    public void onDelete(TodoModel todo);
    public void onComplete(TodoModel todo);
}
