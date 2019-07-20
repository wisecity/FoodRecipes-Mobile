package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRecipeImagesActivity extends AppCompatActivity {

    ListView listViewRecipe;

    ArrayList<String> recipeImageTagFromServer;
    ArrayList<Bitmap> recipeImageFromServer;
    PostClass postClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_images);

        listViewRecipe = findViewById(R.id.listViewRecipe);
        recipeImageTagFromServer = new ArrayList<>();
        recipeImageFromServer = new ArrayList<>();

        postClass = new PostClass(recipeImageTagFromServer, recipeImageFromServer, this);
        listViewRecipe.setAdapter(postClass);
    }

    private void download() {
        
    }
}
