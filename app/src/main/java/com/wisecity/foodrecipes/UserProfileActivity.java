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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

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
    ImageButton iBAddRecipe;
    EditText etFilter;
    ListView lVAllUserRecipes;
    Context context = this;
    ArrayList<String> tempList = new ArrayList<>();

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

        iBAddRecipe = findViewById(R.id.iBAddRecipe);
        iBAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToAddRecipeActivity(accessToken);
            }
        });
        etFilter = (EditText) findViewById(R.id.eTFilter);
        lVAllUserRecipes = (ListView) findViewById(R.id.lVAllUserRecipes);
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

    protected void switchToAddRecipeActivity(Token accessToken) {
        Intent addRecipeIntent = new Intent(UserProfileActivity.this, AddRecipeActivity.class);
        addRecipeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(addRecipeIntent);
    }

    protected void initializeToken() {
        accessToken = new Token(getAccessTokenFromOtherActivities());
    }

    protected void getRecipes() {
        url = new RestAPIUrl();
        retrofit = url.createRetrofitFromUrl();

        rest = retrofit.create(RestAPI.class);
        Call<JsonArray> call = rest.getUserRecipes(LoginActivity.userName);//LoginActivity.userName
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Gson gson = new Gson(); //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // Gson object can be created like this too.

                JsonArray resultList = response.body(); // ERROR AT HERE resultList is null it is not getting initialized properly it causes error at 92th line
                System.out.println(response.code());
                System.out.println(response.message());
                //System.out.println("RECIPE: " + resultList.get("Recipes")); // DEBUG PRINTING ALL RECIPES TO CONSOLE
                if(resultList.size() > 0) { // Null Control for the user page to enter the profiles of users with zero recipes. (resultList.size() is zero when there is no recipes).
                    JsonElement element = resultList.get(0);
                    //JsonArray array = element.getAsJsonArray();
                    allUserRecipes = new Recipe[resultList.size()];
                    for (int i = 0; i < resultList.size(); i++) {

                        Recipe obj = gson.fromJson((resultList.get(i)).toString(), Recipe.class); // ERROR

                        allUserRecipes[i] = obj;
                        System.out.println(allUserRecipes[i].getRecipeName());
                    }
                    //System.out.println("SUCCESSFUL");
                    final String names[] = new String[allUserRecipes.length];
                    for(int i=0;i<allUserRecipes.length;i++){
                        names[i]=allUserRecipes[i].getRecipeName();
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
                    lVAllUserRecipes.setAdapter(dataAdapter);
                    putAllUserRecipesToList();
                }
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

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempList.clear();
                System.out.println("S:" + s.length());

                for(int i = 0; i < allUserRecipes.length ; i++){
                    if (allUserRecipes[i].getRecipeName().toLowerCase().contains(s.toString().toLowerCase())) {
                        tempList.add(allUserRecipes[i].getRecipeName());
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
                    lVAllUserRecipes.setAdapter(dataAdapter);
                    //lVAllUserRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, tempList));
                }

                else if(tempList.size() == 0){
                    lVAllUserRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, tempList));
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
                    lVAllUserRecipes.setAdapter(dataAdapter);
                    //lVAllUserRecipes.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, names));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lVAllUserRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                builder.setTitle("FoodRecipes");
                builder.setMessage("What Would You Like To Do?");
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EDIT PROCESS
                        edit(allUserRecipes[position].getRecipeName(), allUserRecipes[position].getRecipeDetails(), allUserRecipes[position].getRecipeContents(), allUserRecipes[position].getRecipeId(), allUserRecipes[position].getRecipeTags());
                        // To Refresh Activity After Edit
                        //refreshActivity();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // DELETE PROCESS
                        System.out.println(allUserRecipes[position].getRecipeName() + "IS THE RECIPE TO BE DELETED!");
                        sendDeleteRecipeData(allUserRecipes[position].getRecipeName(), allUserRecipes[position].getRecipeId());
                        // To Refresh Activity After Delete
                        //refreshActivity();
                    }
                });
                builder.setNeutralButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // VIEW PROCESS
                        viewUserRecipe(allUserRecipes[position].getRecipeName(), allUserRecipes[position].getRecipeDetails(), allUserRecipes[position].getRecipeContents(), allUserRecipes[position].getRecipePostTime(), allUserRecipes[position].getRecipeTags(), allUserRecipes[position].getRecipeId(), allUserRecipes[position].getRecipeLikes());
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

    protected void sendDeleteRecipeData(String recipeName, String recipeId) {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", recipeName);

        Call<JsonObject> call = rest.deleteRecipe("Bearer " + accessToken.getAccessToken().replace("\"",""), recipeId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                HttpResponse httpResponse = new HttpResponse(response.code(), response.message()); // body().get("status_code").getAsInt() and body().get("message").toString()
                Toast.makeText(UserProfileActivity.this, httpResponse.toString(), Toast.LENGTH_LONG).show();
                switchToHomeActivity(accessToken);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Error At Sending Deletion Of The Recipe Information To The Server!", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void edit(String recipeName, String recipeDetails, String recipeContents, String recipeId, String recipeTags) {
        // EDIT RETROFIT CODE
        Intent editRecipeActivityIntent = new Intent(getApplicationContext(), EditRecipeActivity.class);
        editRecipeActivityIntent.putExtra("Recipe Name", recipeName);
        editRecipeActivityIntent.putExtra("Recipe Details", recipeDetails);
        editRecipeActivityIntent.putExtra("Recipe Contents", recipeContents);
        editRecipeActivityIntent.putExtra("Recipe Id", recipeId);
        editRecipeActivityIntent.putExtra("Recipe Tags", recipeTags);
        editRecipeActivityIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(editRecipeActivityIntent);
    }

    private void viewUserRecipe(String recipeName, String recipeDetails, String recipeContents, String recipePostTime, String recipeTags, String recipeId, int recipeLikes) {
        // VIEW RETROFIT CODE
        Intent viewUserRecipeActivityIntent = new Intent(getApplicationContext(), ViewUserRecipeActivity.class);
        viewUserRecipeActivityIntent.putExtra("Recipe Name", recipeName);
        viewUserRecipeActivityIntent.putExtra("Recipe Details", recipeDetails);
        viewUserRecipeActivityIntent.putExtra("Recipe Contents", recipeContents);
        viewUserRecipeActivityIntent.putExtra("Post Time", recipePostTime);
        viewUserRecipeActivityIntent.putExtra("Recipe Tags", recipeTags);
        viewUserRecipeActivityIntent.putExtra("Recipe Likes", recipeLikes);
        viewUserRecipeActivityIntent.putExtra("Recipe Id", recipeId);
        startActivity(viewUserRecipeActivityIntent);
    }


    protected void refreshActivity() {
        finish();
        startActivity(getIntent());
    }
}
