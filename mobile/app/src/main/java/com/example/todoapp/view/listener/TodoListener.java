package com.example.todoapp.view.listener;

import com.example.todoapp.service.model.TodoModel;

public interface TodoListener {
    public void onEdit(TodoModel todo);
    public void onDelete(TodoModel todo);
    public void onComplete(TodoModel todo);
}
