package com.wisecity.foodrecipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

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

    @GET("/showrecipes")
    Call<JsonObject> getAllRecipes();

    @GET("user/{username}")
    Call<JsonArray> getUserRecipes(@Path("username") String username);

    @POST("/recipe") //@Headers("Authorization: {Bearer}")
    Call<JsonObject> addRecipe( @Header("Token") String accessToken, @Body JsonObject body);

}
