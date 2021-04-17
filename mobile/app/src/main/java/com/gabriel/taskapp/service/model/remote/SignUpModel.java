package com.gabriel.taskapp.service.model.remote;

import com.google.gson.annotations.SerializedName;

import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_MESSAGE;
import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_STATUS;
import static com.gabriel.taskapp.service.constants.PersonConstants.PERSON_SUCCESS;

public class SignUpModel {
    @SerializedName(PERSON_MESSAGE)
    public String message = "";

    @SerializedName(PERSON_SUCCESS)
    public boolean success = false;

    @SerializedName(PERSON_STATUS)
    public String status = "";
}
