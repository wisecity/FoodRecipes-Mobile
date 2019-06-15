package com.wisecity.foodrecipes;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Recipe {

    @SerializedName("id")
    private int recipeId;

    @SerializedName("name")
    private String recipeName;

    @SerializedName("post_time")
    private String recipePostTime;

    @SerializedName("contents")
    private String recipeContents;

    @SerializedName("details")
    private String recipeDetails;

    @SerializedName("views")
    private int recipeViews;

    @SerializedName("score")
    private int recipeScore;

    @SerializedName("user_id")
    private int userId;


    public String getRecipeName() {
        return recipeName;
    }

    public Calendar getRecipePostTime() throws Exception {
        return DateTime.toCalendar(recipePostTime);
    }

    public String getRecipeDetails() {
        return recipeDetails;
    }

    public String getRecipeContents() {
        return recipeContents;
    }

    public int getRecipeViews() {
        return recipeViews;
    }

    public int getRecipeScore() {
        return recipeScore;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }
}
