package com.wisecity.foodrecipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RestAPI {

    @Headers({
            "Accept: application/json"
    })
    @POST("/api/register")
    Call<JsonObject> saveRegister(@Body JsonObject body);

    @Headers({
            "Accept: application/json"
    })
    @POST("/api/login")
    Call<JsonObject> sendLogin(@Body JsonObject body);

    @GET("/api/showAllRecipes")
    Call<JsonArray> getAllRecipes();

    @GET("/api/getUserRecipes/{username}")
    Call<JsonArray> getUserRecipes(@Path("username") String username);

    @Headers({ "Content-Type: application/json"})
    @POST("/api/addRecipe")
    Call<JsonObject> addRecipe( @Header("Authorization") String accessToken, @Body JsonObject body);

    @Headers({ "Content-Type: application/json"})
    @PUT("/api/recipeManipulation/{recipe_id}")
    Call<JsonObject> editRecipe( @Header("Authorization") String accessToken, @Path("recipe_id") String recipeId, @Body JsonObject body);

    @Headers({ "Content-Type: application/json"})
    @DELETE("/api/recipeManipulation/{recipe_id}")
    Call<JsonObject> deleteRecipe( @Header("Authorization") String accessToken, @Path("recipe_id") String recipeId);

    @POST("/api/{recipe_id}/like")
    Call<JsonObject> likeRecipe(@Path("recipe_id") String recipeId);

}
