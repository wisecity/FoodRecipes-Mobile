package com.wisecity.foodrecipes;

import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Recipe {

    @SerializedName("id")
    private String recipeId;

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

    @SerializedName("likes")
    private int recipeLikes;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("tags")
    private String tags;


    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipePostTime() {
        return recipePostTime;
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

    public int getRecipeLikes() {
        return recipeLikes;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public String getRecipeTags() {
        return tags;
    }
}
