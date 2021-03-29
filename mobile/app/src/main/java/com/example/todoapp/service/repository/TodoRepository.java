package com.example.todoapp.service.repository;

import io.realm.Realm;

public class TodoRepository {
    private static TodoRepository repository = null;

    private TodoRepository(){ }

    public static TodoRepository getTodoRepository(){
        if(repository == null){
            repository = new TodoRepository();
        }
        return repository;
    }




}
