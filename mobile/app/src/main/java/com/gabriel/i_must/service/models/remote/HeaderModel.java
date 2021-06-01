package com.gabriel.i_must.service.models.remote;

import com.google.gson.annotations.SerializedName;

import static com.gabriel.i_must.service.constants.APIConstants.API_EMAIL;
import static com.gabriel.i_must.service.constants.APIConstants.API_STATUS;
import static com.gabriel.i_must.service.constants.APIConstants.API_SUCCESS;
import static com.gabriel.i_must.service.constants.APIConstants.API_TOKEN;

public class HeaderModel {
    @SerializedName(API_TOKEN)
    public String token = "";

    @SerializedName(API_EMAIL)
    public String email = "";

    @SerializedName(API_SUCCESS)
    public boolean success = false;

    @SerializedName(API_STATUS)
    public String status = "";
}
