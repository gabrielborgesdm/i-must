package com.gabriel.taskapp.service.models.local;

import android.content.Context;

import com.gabriel.taskapp.service.models.remote.RemoteTaskModel;
import com.gabriel.taskapp.service.repositories.ImageRepository;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LocalTaskModel extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String description;
    private boolean completed;
    private String datetime;
    private RealmList<String> imagePaths;
    private boolean removed;
    private long lastSync;
    private long lastUpdated;

    public LocalTaskModel() {
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

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void updateLastUpdated() {
        setLastUpdated(System.currentTimeMillis());
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public ArrayList<String> getImagePaths() {
        return new ArrayList<>(this.imagePaths);
    }

    public void setImagePaths(ArrayList<String> imagePathsArrayList) {
        RealmList<String> imagePaths = new RealmList<>();
        imagePaths.addAll(imagePathsArrayList);
        this.imagePaths = imagePaths;
    }

    public void setValuesFromRemoteTask(Context context, RemoteTaskModel remoteTask) {
        this.setId(remoteTask.getId());
        this.setCompleted(remoteTask.getCompleted());
        this.setDescription(remoteTask.getDescription());
        if(remoteTask.getLastUpdated() != null) this.setLastUpdated(this.getLastUpdated());
        if (remoteTask.getDatetime() != null) this.setDatetime(remoteTask.getDatetime());
        if (remoteTask.getImages() != null) {
            ImageRepository imageRepository = ImageRepository.getRepository(context);
            ArrayList<String> images = remoteTask.getImages();
            this.setImagePaths(imageRepository.decodeImagesAndGetPaths(images));
        }
    }
}
