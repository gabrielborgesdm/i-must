package com.gabriel.taskapp.service.models.remote;

import com.gabriel.taskapp.service.models.local.LocalTaskModel;
import com.google.gson.annotations.SerializedName;

import static com.gabriel.taskapp.service.constants.APIConstants.API_MESSAGE;
import static com.gabriel.taskapp.service.constants.APIConstants.API_STATUS;
import static com.gabriel.taskapp.service.constants.APIConstants.API_SUCCESS;
import static com.gabriel.taskapp.service.constants.APIConstants.API_TASK;

public class ResponseTaskModel {
    @SerializedName(API_MESSAGE)
    public String message = "";

    @SerializedName(API_SUCCESS)
    public boolean success = false;

    @SerializedName(API_STATUS)
    public String status = "";

    @SerializedName(API_TASK)
    public LocalTaskModel task = new LocalTaskModel();
}
