package com.gabriel.taskapp.service.model.remote;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.gabriel.taskapp.service.constants.APIConstants.API_COMPLETED;
import static com.gabriel.taskapp.service.constants.APIConstants.API_CREATED_AT;
import static com.gabriel.taskapp.service.constants.APIConstants.API_DATETIME;
import static com.gabriel.taskapp.service.constants.APIConstants.API_DESCRIPTION;
import static com.gabriel.taskapp.service.constants.APIConstants.API_ID;
import static com.gabriel.taskapp.service.constants.APIConstants.API_IMAGES;
import static com.gabriel.taskapp.service.constants.APIConstants.API_LAST_UPDATED;
import static com.gabriel.taskapp.service.constants.APIConstants.API_USER_ID;

public class RemoteTaskModel {
    @SerializedName(API_ID)
    public String id = null;

    @SerializedName(API_USER_ID)
    public String userId = null;

    @SerializedName(API_COMPLETED)
    public Boolean completed = null;

    @SerializedName(API_DATETIME)
    public String datetime = null;

    @SerializedName(API_DESCRIPTION)
    public String description = null;

    @SerializedName(API_LAST_UPDATED)
    public Long lastUpdated = null;

    @SerializedName(API_CREATED_AT)
    public Long createdAt = null;

    @SerializedName(API_IMAGES)
    public ArrayList<String> images = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}