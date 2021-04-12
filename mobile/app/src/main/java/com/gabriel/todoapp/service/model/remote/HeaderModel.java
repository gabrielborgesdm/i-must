package com.gabriel.todoapp.service.model.remote;

import com.google.gson.annotations.SerializedName;

import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_STATUS;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_SUCCESS;
import static com.gabriel.todoapp.service.constants.PersonConstants.PERSON_TOKEN;

public class HeaderModel {
    @SerializedName(PERSON_TOKEN)
    public String token = "";

    @SerializedName(PERSON_SUCCESS)
    public boolean success = false;

    @SerializedName(PERSON_STATUS)
    public String status = "";
}
