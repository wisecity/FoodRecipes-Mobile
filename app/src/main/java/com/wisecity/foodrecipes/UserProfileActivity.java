package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity {

    private RestAPI rest;
    private RestAPIUrl url;
    private Retrofit retrofit;
    private Token accessToken;
    private Recipe[] allUserRecipes;
    ImageButton iBHome;

    ListView lVAllUserRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initializeToken();
        iBHome = findViewById(R.id.iBHome);
        iBHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHomeActivity(accessToken);
            }
        });

        lVAllUserRecipes = findViewById(R.id.lVAllUserRecipes);
        getRecipes();
    }

    protected String getAccessTokenFromOtherActivities() {
        Intent fromOtherIntent = getIntent();
        String accessToken = fromOtherIntent.getStringExtra("accessToken");
        System.out.println("DEBUG, TOKEN IS:" + accessToken);
        return accessToken;
    }

    protected void switchToHomeActivity(Token accessToken) {
        Intent userHomeIntent = new Intent(UserProfileActivity.this, HomeActivity.class);
        userHomeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(userHomeIntent);
    }

    protected void initializeToken() {
        accessToken = new Token(getAccessTokenFromOtherActivities());
    }

    protected void getRecipes() {
        url = new RestAPIUrl();
        retrofit = url.createRetrofitFromUrl();

        rest = retrofit.create(RestAPI.class);
        System.out.println("DEBUg 1: " + LoginActivity.userName);
        Call<JsonArray> call = rest.getUserRecipes(LoginActivity.userName);//LoginActivity.userName
        System.out.println("DEBUg 2");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Gson gson = new Gson(); //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // Gson object can be created like this too.

                JsonArray resultList = response.body(); // ERROR AT HERE resultList is null it is not getting initialized properly it causes error at 92th line
                System.out.println(response.code());
                System.out.println(response.message());
                //System.out.println("RECIPE: " + resultList.get("Recipes")); // DEBUG PRINTING ALL RECIPES TO CONSOLE
                JsonElement element = resultList.get(0);
                //JsonArray array = element.getAsJsonArray();
                allUserRecipes = new Recipe[resultList.size()];
                for(int i=0;i<resultList.size();i++){

                    Recipe obj = gson.fromJson((resultList.get(i)).toString(),Recipe.class); // ERROR

                    allUserRecipes[i] = obj;
                    System.out.println(allUserRecipes[i].getRecipeName());
                }
                //System.out.println("SUCCESSFUL");
                putAllUserRecipesToList();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("FAILED");
            }
        });

    }

    private void putAllUserRecipesToList() {
        final String names[] = new String[allUserRecipes.length];
        for(int i=0;i<allUserRecipes.length;i++){
            names[i]=allUserRecipes[i].getRecipeName();
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
        lVAllUserRecipes.setAdapter(dataAdapter);
    }
}
