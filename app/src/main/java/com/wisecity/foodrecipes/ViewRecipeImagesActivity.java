package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Retrofit;

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
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();
        RestAPI rest = retrofit.create(RestAPI.class);
    }

    // MENU PROCESSES

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout) {
            // DO THE LOGOUT PROCESSES HERE FOR NOW THERE IS ONLY REDIRECTION OF INTENTS
            Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Logout Successfully Completed!", Toast.LENGTH_LONG).show();
            startActivity(loginActivityIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
