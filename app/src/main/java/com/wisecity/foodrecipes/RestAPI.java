package com.wisecity.foodrecipes;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestAPI {

    @Headers({
            "Accept: application/json"
    })
    @POST("register")
    Call<JsonObject> saveRegister(@Body JsonObject body);

    @Headers({
            "Accept: application/json"
    })
    @POST("login")
    Call<JsonObject> sendLogin(@Body JsonObject body);

}