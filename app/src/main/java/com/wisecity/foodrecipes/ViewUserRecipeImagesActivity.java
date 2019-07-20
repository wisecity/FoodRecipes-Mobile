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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewUserRecipeImagesActivity extends AppCompatActivity {

    ListView listViewRecipe;

    private String recipeId;
    private Token accessToken;

    ArrayList<String> urlUserFinalPhoto;
    ArrayList<String> urlUserIngredientPhoto;
    ArrayList<String> urlUserStepPhoto;

    ArrayList<String> userRecipeImageTagFromServer;
    ArrayList<Bitmap> userRecipeImageFromServer;
    PostClass postClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_recipe_images);

        initializeToken();
        initializeRecipeId();

        urlUserFinalPhoto = new ArrayList<>();
        urlUserIngredientPhoto = new ArrayList<>();
        urlUserStepPhoto = new ArrayList<>();

        listViewRecipe = findViewById(R.id.listViewRecipe);
        userRecipeImageTagFromServer = new ArrayList<>();
        userRecipeImageFromServer = new ArrayList<>();

        postClass = new PostClass(userRecipeImageTagFromServer, userRecipeImageFromServer, this);
        listViewRecipe.setAdapter(postClass);

        download();
    }

    private void download() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();
        RestAPI rest = retrofit.create(RestAPI.class);
        Call<JsonArray> callForFinalPhoto = rest.getRecipeFinalPhoto(recipeId);
        callForFinalPhoto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                //for(int i = 0; i < response.body().size(); i++) {
                //urlUserFinalPhoto.add(i, response.body().);
                //}
                Toast.makeText(getApplicationContext(), "Final Image Has Been Downloaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Downloading Recipe Image From The Server!", Toast.LENGTH_LONG).show();
            }
        });

        Call<JsonArray> callForIngredientPhoto = rest.getRecipeIngredientPhoto(recipeId);
        callForIngredientPhoto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Toast.makeText(getApplicationContext(), "Ingredient Image Has Been Downloaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Downloading Recipe Ingredient Image From The Server!", Toast.LENGTH_LONG).show();
            }
        });

        Call<JsonArray> callForStepPhoto = rest.getRecipeStepPhoto(recipeId);
        callForStepPhoto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Toast.makeText(getApplicationContext(), "Recipe Step Image Has Been Downloaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Downloading Recipe Step Image From The Server!", Toast.LENGTH_LONG).show();
            }
        });
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

    private String getAccessTokenFromOtherActivities() {
        Intent fromOtherIntent = getIntent();
        String accessToken = fromOtherIntent.getStringExtra("accessToken");
        System.out.println("DEBUG, TOKEN IS:" + accessToken);
        return accessToken;
    }

    private void initializeRecipeId() {
        Bundle bundleViewRecipe = getIntent().getExtras();
        recipeId = bundleViewRecipe.getString("Recipe Id");
    }

    private void initializeToken() {
        accessToken = new Token(getAccessTokenFromOtherActivities());
    }
}
