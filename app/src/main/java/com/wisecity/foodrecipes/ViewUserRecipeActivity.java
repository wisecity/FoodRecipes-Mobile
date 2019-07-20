package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewUserRecipeActivity extends AppCompatActivity {

    ImageButton iBUserProfile;
    ImageButton iBUrl;
    ImageButton iBLike;
    private Token accessToken;

    private String recipeName;
    private String recipeContents;
    private String recipeDetails;
    private String recipePostTime;
    private String recipeTags;
    private int recipeLikes;
    private String recipeId;

    EditText eTRecipeName;
    EditText eTRecipeContents;
    EditText eTRecipeDetails;
    EditText eTRecipePostTime;
    EditText eTRecipeTags;
    EditText eTRecipeLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_recipe);

        initializeToken();
        iBUserProfile = findViewById(R.id.iBUserProfile);
        iBUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToUserProfileActivity(accessToken);
            }
        });

        iBUrl = findViewById(R.id.iBUrl);
        iBUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUrl();
            }
        });

        iBLike = findViewById(R.id.iBLike);
        iBLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeRecipe();
            }
        });

        eTRecipeName = findViewById(R.id.eTRecipeName);
        eTRecipeContents = findViewById(R.id.eTRecipeContents);
        eTRecipeDetails = findViewById(R.id.eTRecipeDetails);
        eTRecipePostTime = findViewById(R.id.eTRecipePostTime);
        eTRecipeTags = findViewById(R.id.eTRecipeTags);
        eTRecipeLikes = findViewById(R.id.eTRecipeLikes);

        getInitialRecipeInfo();

        eTRecipeName.setEnabled(false);
        eTRecipeContents.setEnabled(false);
        eTRecipeDetails.setEnabled(false);
        eTRecipePostTime.setEnabled(false);
        eTRecipeTags.setEnabled(false);
        eTRecipeLikes.setEnabled(false);
    }

    protected String getAccessTokenFromOtherActivities() {
        Intent fromOtherIntent = getIntent();
        String accessToken = fromOtherIntent.getStringExtra("accessToken");
        System.out.println("DEBUG, TOKEN IS:" + accessToken);
        return accessToken;
    }

    protected void initializeToken() {
        accessToken = new Token(getAccessTokenFromOtherActivities());
    }

    protected void switchToUserProfileActivity(Token accessToken) {
        Intent userProfileIntent = new Intent(ViewUserRecipeActivity.this, UserProfileActivity.class);
        userProfileIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(userProfileIntent);
    }

    protected void getInitialRecipeInfo() {
        Bundle bundleViewRecipe = getIntent().getExtras();
        recipeName = bundleViewRecipe.getString("Recipe Name");
        recipeContents = bundleViewRecipe.getString("Recipe Contents");
        recipeDetails = bundleViewRecipe.getString("Recipe Details");
        recipePostTime = bundleViewRecipe.getString("Post Time");
        recipeTags = bundleViewRecipe.getString("Recipe Tags");
        recipeLikes = bundleViewRecipe.getInt("Recipe Likes");
        recipeId = bundleViewRecipe.getString("Recipe Id");
        eTRecipeName.setText(recipeName);
        eTRecipeContents.setText(recipeContents);
        eTRecipeDetails.setText(recipeDetails);
        eTRecipePostTime.setText(recipePostTime);
        eTRecipeTags.setText(recipeTags);
        eTRecipeLikes.setText("Likes: " + recipeLikes);
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

    private void shareUrl() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://foodrecipesbil495.herokuapp.com/recipedetails/" + recipeId);
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    private void likeRecipe() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();
        RestAPI rest = retrofit.create(RestAPI.class);
        Call<JsonObject> call = rest.likeRecipe(recipeId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Toast.makeText(getApplicationContext(), "Recipe Has Been Successfully Liked", Toast.LENGTH_LONG).show();
                switchToUserProfileActivity(accessToken);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Sending Recipe Like Information To Server!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void switchToUserRecipeImages(Token accessToken) {
        Intent viewUserRecipeImagesIntent = new Intent(ViewUserRecipeActivity.this, ViewUserRecipeImagesActivity.class);
        viewUserRecipeImagesIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(viewUserRecipeImagesIntent);
    }

    private void viewUserRecipeImages(View view) {
        switchToUserRecipeImages(accessToken);
    }
}
