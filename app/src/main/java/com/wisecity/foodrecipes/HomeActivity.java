package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private Token accessToken;
    ImageButton iBUserProfile;
    private Recipe[] allRecipes;

    private RestAPI rest;
    private RestAPIUrl url;
    private Retrofit retrofit;

    ListView lstAllRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toast.makeText(HomeActivity.this, "Welcome!", Toast.LENGTH_LONG).show();

        initializeToken();

        iBUserProfile = findViewById(R.id.iBUserProfile);
        iBUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToUserProfileActivity(accessToken);
            }
        });

        lstAllRecipes = findViewById(R.id.lstAllRecipes);
        getRecipes();

    }

    protected String getAccessTokenFromOtherActivities() {
        Intent fromOtherIntent = getIntent();
        String accessToken = fromOtherIntent.getStringExtra("accessToken");
        //System.out.println("DEBUG, TOKEN IS:" + accessToken);
        return accessToken;
    }

    protected void switchToUserProfileActivity(Token accessToken) {
        Intent userProfileIntent = new Intent(HomeActivity.this, UserProfileActivity.class);
        userProfileIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(userProfileIntent);
    }

    protected void initializeToken() {
        accessToken = new Token(getAccessTokenFromOtherActivities());
    }


    protected void getRecipes() {
        url = new RestAPIUrl();
        retrofit = url.createRetrofitFromUrl();

        rest = retrofit.create(RestAPI.class);

        Call<JsonObject> call = rest.getAllRecipes();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Gson gson = new Gson(); //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // Gson object can be created like this too.

                JsonObject resultList = response.body();
                //System.out.println("RECIPE: " + resultList.get("Recipes")); // DEBUG PRINTING ALL RECIPES TO CONSOLE
                JsonElement element = resultList.get("Recipes");
                JsonArray array = element.getAsJsonArray();
                //System.out.println(array.size()); // DEBUG
                allRecipes = new Recipe[array.size()];
                for(int i=0;i<array.size();i++){

                    Recipe obj = gson.fromJson((array.get(i)).toString(),Recipe.class); // ERROR

                    allRecipes[i] = obj;
                    //System.out.println(allRecipes[i].getRecipeName());
                }
                //System.out.println("SUCCESSFUL");
                putAllRecipesToList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("FAILED");
            }
        });
    }

    private void putAllRecipesToList() {
        final String names[] = new String[allRecipes.length];
        for(int i=0;i<allRecipes.length;i++){
            names[i]=allRecipes[i].getRecipeName();
        }

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, names){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item


                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#ffffff"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);


                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

                ViewGroup.LayoutParams layoutparams = item.getLayoutParams();
                layoutparams.height = 170;

                item.setLayoutParams(layoutparams);


                // return the view
                return item;
            }
        };
        lstAllRecipes.setAdapter(dataAdapter);
    }
}
