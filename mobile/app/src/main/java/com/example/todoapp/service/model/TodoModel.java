package com.example.todoapp.service.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TodoModel extends RealmObject {
    @PrimaryKey
    private String id;

    @Required
    private String description;

    @Required
    private boolean completed;

    public TodoModel(final String id, final String description, final boolean completed ){
        this.setId(id);
        this.setDescription(description);
        this.setCompleted(completed);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
