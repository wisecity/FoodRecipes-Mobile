package com.wisecity.foodrecipes;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestAPI {

    @Headers({
            "Accept: application/json"
    })
    @POST("user")
    Call<JsonObject> saveRegister(@Body JsonObject body);

    @Headers({
            "Accept: application/json"
    })
    @POST("loginnn")
    Call<JsonObject> sendLogin(@Body JsonObject body);
}
