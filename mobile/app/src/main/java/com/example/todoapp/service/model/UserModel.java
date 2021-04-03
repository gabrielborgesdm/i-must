package com.example.todoapp.service.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserModel extends RealmObject {
    @PrimaryKey
    @Required
    private String id;

    @Required
    private String name;

    @Required
    private String email;

    @Required
    private String password;

    public UserModel() { this.id = UUID.randomUUID().toString(); }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
