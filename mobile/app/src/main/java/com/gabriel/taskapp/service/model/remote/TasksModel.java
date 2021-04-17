package com.gabriel.taskapp.service.model.remote;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gabriel.taskapp.service.constants.APIConstants.API_MESSAGE;
import static com.gabriel.taskapp.service.constants.APIConstants.API_STATUS;
import static com.gabriel.taskapp.service.constants.APIConstants.API_SUCCESS;
import static com.gabriel.taskapp.service.constants.APIConstants.API_TASKS;

public class TasksModel {
    @SerializedName(API_MESSAGE)
    public String message = "";

    @SerializedName(API_SUCCESS)
    public boolean success = false;

    @SerializedName(API_STATUS)
    public String status = "";

    @SerializedName(API_TASKS)
    public List<TaskModel> tasks = new ArrayList<>();
}
