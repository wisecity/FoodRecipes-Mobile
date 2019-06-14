package com.wisecity.foodrecipes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Recipe {

    @SerializedName("name")
    private String recipeName;

    @SerializedName("post_time")
    private Date recipePostTime;

    @SerializedName("details")
    private String recipeDetails;

    @SerializedName("tag")
    private String recipeTag;

    @SerializedName("contents")
    private String recipeContents;

    @SerializedName("views")
    private int recipeViews;

    @SerializedName("score")
    private int recipeScore;


    public String getRecipeName() {
        return recipeName;
    }

    public Date getRecipePostTime() {
        return recipePostTime;
    }

    public String getRecipeDetails() {
        return recipeDetails;
    }

    public String getRecipeTag() {
        return recipeTag;
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
}
