package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewUserRecipeImagesActivity extends AppCompatActivity {

    ListView listViewRecipe;

    ArrayList<String> userRecipeImageTagFromServer;
    ArrayList<Bitmap> userRecipeImageFromServer;
    PostClass postClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_recipe_images);

        listViewRecipe = findViewById(R.id.listViewRecipe);
        userRecipeImageTagFromServer = new ArrayList<>();
        userRecipeImageFromServer = new ArrayList<>();

        postClass = new PostClass(userRecipeImageTagFromServer, userRecipeImageFromServer, this);
        listViewRecipe.setAdapter(postClass);
    }

    private void download() {

    }
}
