package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

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
    EditText eTFilter;
    Context context = this;
    ArrayList<String> tempList = new ArrayList<>();

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

        eTFilter = (EditText) findViewById(R.id.eTFilter);
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

        Call<JsonArray> call = rest.getAllRecipes();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Gson gson = new Gson(); //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // Gson object can be created like this too.

                JsonArray resultList = response.body();
                //System.out.println("RECIPE: " + resultList.get("Recipes")); // DEBUG PRINTING ALL RECIPES TO CONSOLE
                //JsonElement element = resultList.get("Recipes");
                JsonArray array = resultList; //element.getAsJsonArray();
                //System.out.println(array.size()); // DEBUG
                allRecipes = new Recipe[array.size()];
                for(int i=0;i<array.size();i++){

                    Recipe obj = gson.fromJson((array.get(i)).toString(),Recipe.class); // ERROR

                    allRecipes[i] = obj;
                    //System.out.println(allRecipes[i].getRecipeName());
                }

                final String names[] = new String[allRecipes.length];
                for(int i=0;i<allRecipes.length;i++){
                    names[i]=allRecipes[i].getRecipeName();
                }
                ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>
                        (context, android.R.layout.simple_list_item_1, android.R.id.text1, names){

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
                //System.out.println("SUCCESSFUL");
                putAllRecipesToList();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                System.out.println("FAILED");
            }
        });
    }

    private void putAllRecipesToList() {
        final String names[] = new String[allRecipes.length];
        for(int i=0;i<allRecipes.length;i++){
            names[i]=allRecipes[i].getRecipeName();
        }

        eTFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempList.clear();
                System.out.println("S:" + s.length());

                for(int i = 0; i < allRecipes.length ; i++){
                    if (allRecipes[i].getRecipeName().toLowerCase().contains(s.toString().toLowerCase())) {
                        tempList.add(allRecipes[i].getRecipeName());
                    }

                }
                if(tempList != null && tempList.size() > 0){
                    ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>
                            (context, android.R.layout.simple_list_item_1, android.R.id.text1, tempList){

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
                    //lVAllUserRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, tempList));
                }

                else if(tempList.size() == 0){
                    lstAllRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, tempList));
                }

                else if(s.length() == 0){
                    ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>
                            (context, android.R.layout.simple_list_item_1, android.R.id.text1, names){

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
                    //lVAllUserRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, names));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        lstAllRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("FoodRecipes");
                builder.setMessage("Would You Like To View The Recipe?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do Nothing.
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Send To Recipe Page
                        view(allRecipes[position].getRecipeName(), allRecipes[position].getRecipeDetails(), allRecipes[position].getRecipeContents(), allRecipes[position].getRecipePostTime(), allRecipes[position].getRecipeTags(), allRecipes[position].getRecipeId());
                        //refreshActivity();
                    }
                });
                builder.create().show();
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

    protected void view(String recipeName, String recipeDetails, String recipeContents, String recipePostTime, String recipeTags, String recipeId) {
        // VIEW RETROFIT CODE
        Intent viewRecipeActivityIntent = new Intent(getApplicationContext(), ViewRecipeActivity.class);
        viewRecipeActivityIntent.putExtra("Recipe Name", recipeName);
        viewRecipeActivityIntent.putExtra("Recipe Details", recipeDetails);
        viewRecipeActivityIntent.putExtra("Recipe Contents", recipeContents);
        viewRecipeActivityIntent.putExtra("Post Time", recipePostTime);
        viewRecipeActivityIntent.putExtra("Recipe Tags", recipeTags);
        viewRecipeActivityIntent.putExtra("Recipe Id", recipeId);
        startActivity(viewRecipeActivityIntent);
    }

}
