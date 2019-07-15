package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditRecipeActivity extends AppCompatActivity {

    private Token accessToken;

    private String recipeName;
    private String recipeContents;
    private String recipeDetails;
    private String recipeDate;
    private String recipeId;

    EditText eTRecipeName;
    EditText eTRecipeContents;
    EditText eTRecipeDetails;
    Button btnEditRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        initializeToken();

        eTRecipeName = findViewById(R.id.eTRecipeName);
        eTRecipeContents = findViewById(R.id.eTRecipeContents);
        eTRecipeDetails = findViewById(R.id.eTRecipeDetails);
        getInitialRecipeInfo();

        btnEditRecipe = findViewById(R.id.btnEditRecipe);
        btnEditRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(eTRecipeName.getText().toString().equals(""))) {
                    recipeName = eTRecipeName.getText().toString();
                    recipeContents = eTRecipeContents.getText().toString();
                    recipeDetails = eTRecipeDetails.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    recipeDate = formatter.format(calendar.getTime());

                    sendEditRecipeData();
                }
                else {
                    Toast.makeText(getApplicationContext() ,"Recipe Has Not Been Edited: Please Enter Recipe Information And Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });


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

    protected void switchToHomeActivity(Token accessToken) {
        Intent editRecipeHomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        editRecipeHomeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(editRecipeHomeIntent);
    }

    private void sendEditRecipeData() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", recipeName);
        jsonObj.addProperty("contents", recipeContents);
        jsonObj.addProperty("details", recipeDetails);
        jsonObj.addProperty("post_time", recipeDate);

        Call<JsonObject> call = rest.editRecipe("Bearer " + accessToken.getAccessToken().replace("\"",""), recipeId, jsonObj);

        System.out.println(call.request().toString());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                HttpResponse httpResponse = new HttpResponse(response.code(), response.message()); // body().get("status_code").getAsInt() and body().get("message").toString()
                Toast.makeText(getApplicationContext(), httpResponse.toString(), Toast.LENGTH_LONG).show();
                System.out.println("BURAYA GIRIYOR");
                switchToHomeActivity(accessToken);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Sending Recipe Information To Server!", Toast.LENGTH_LONG).show();
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

    protected void getInitialRecipeInfo() {
        Bundle bundleEditRecipe = getIntent().getExtras();
        recipeName = bundleEditRecipe.getString("Recipe Name");
        recipeContents = bundleEditRecipe.getString("Recipe Contents");
        recipeDetails = bundleEditRecipe.getString("Recipe Details");
        recipeId = bundleEditRecipe.getString("Recipe Id");
        eTRecipeName.setText(recipeName);
        eTRecipeContents.setText(recipeContents);
        eTRecipeDetails.setText(recipeDetails);
    }
}