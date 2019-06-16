package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddRecipeActivity extends AppCompatActivity {

    private Token accessToken;

    private String recipeName;
    private String recipeContents;
    private String recipeDetails;

    EditText eTRecipeName;
    EditText eTRecipeContents;
    EditText eTRecipeDetails;
    Button btnAddRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        initializeToken();

        eTRecipeName = findViewById(R.id.eTRecipeName);
        eTRecipeContents = findViewById(R.id.eTRecipeContents);
        eTRecipeDetails = findViewById(R.id.eTRecipeDetails);

        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(eTRecipeName.getText().toString().equals(""))) {
                    recipeName = eTRecipeName.getText().toString();
                    recipeContents = eTRecipeContents.getText().toString();
                    recipeDetails = eTRecipeDetails.getText().toString();

                    sendAddRecipeData();
                }
                else {
                    Toast.makeText(AddRecipeActivity.this ,"Recipe Not Added: Please Enter Recipe Information And Try Again!", Toast.LENGTH_LONG).show();
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
        Intent addRecipeHomeIntent = new Intent(AddRecipeActivity.this, HomeActivity.class);
        addRecipeHomeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(addRecipeHomeIntent);
    }

    private void sendAddRecipeData() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", recipeName);
        jsonObj.addProperty("contents", recipeContents);
        jsonObj.addProperty("details", recipeDetails);

        System.out.println("DEBUG 1:");
        Call<JsonObject> call = rest.addRecipe(jsonObj,"Bearer" + accessToken.getAccessToken());
        System.out.println("DEBUG 2: " + jsonObj);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HttpResponse httpResponse = new HttpResponse(response.code(), response.message()); // body().get("status_code").getAsInt() and body().get("message").toString()
                Toast.makeText(AddRecipeActivity.this, httpResponse.toString(), Toast.LENGTH_LONG).show();
                switchToHomeActivity(accessToken);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddRecipeActivity.this, "Error At Sending Recipe Information To Server!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
