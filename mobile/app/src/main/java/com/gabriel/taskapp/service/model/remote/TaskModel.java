package com.gabriel.taskapp.service.model.remote;

import com.google.gson.annotations.SerializedName;

import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_COMPLETED;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_DESCRIPTION;
import static com.gabriel.taskapp.service.constants.TaskConstants.TASK_ID;

public class TaskModel {

    @SerializedName(TASK_ID)
    public String id = "";

    @SerializedName(TASK_DESCRIPTION)
    public String description = "";

    @SerializedName(TASK_COMPLETED)
    public Boolean completed = false;

}
