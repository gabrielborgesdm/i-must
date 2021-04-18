package com.gabriel.taskapp.service.model.local;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class TaskModel extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String description;
    private boolean completed;
    private boolean removed;
    private long lastSync;

    public TaskModel() {
        this.id = UUID.randomUUID().toString();
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

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }

    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) {  this.removed = removed; }
}
