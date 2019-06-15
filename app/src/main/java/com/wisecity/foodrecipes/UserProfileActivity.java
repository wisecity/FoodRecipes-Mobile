package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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

        Call<JsonObject> call = rest.getUserRecipes(accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Gson gson = new Gson(); //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // Gson object can be created like this too.

                JsonObject resultList = response.body(); // ERROR AT HERE resultList is null it is not getting initialized properly it causes error at 92th line
                System.out.println(response.code());
                System.out.println(response.message());
                //System.out.println("RECIPE: " + resultList.get("Recipes")); // DEBUG PRINTING ALL RECIPES TO CONSOLE
                JsonElement element = resultList.get("access_token");
                JsonArray array = element.getAsJsonArray();
                //System.out.println(array.size()); // DEBUG
                allUserRecipes = new Recipe[array.size()];
                for(int i=0;i<array.size();i++){

                    Recipe obj = gson.fromJson((array.get(i)).toString(),Recipe.class); // ERROR

                    allUserRecipes[i] = obj;
                    System.out.println(allUserRecipes[i].getRecipeName());
                }
                //System.out.println("SUCCESSFUL");
                putAllUserRecipesToList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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

        /*
        lstAllRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {


                AlertDialog.Builder desc =
                        new AlertDialog.Builder(HomeActivity.this);

                desc.setMessage(allRecipes[position].getRecipeDetails())
                        .setCancelable(true);


                desc.setNeutralButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final String recipeId = allRecipes[position].getRecipe_id();
                                deleteRecipe(recipeId);
                                finish();
                                startActivity(getIntent());

                                dialog.cancel();
                            }
                        });

                desc.setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                recipe_id = recipes[position].getRecipe_id();
                                recipe_name = recipes[position].getRecipe_name();
                                recipe_desc = recipes[position].getRecipe_desc();


                                AlertDialog.Builder updateAlert =
                                        new AlertDialog.Builder(RecipePageActivity.this);



                                final EditText inputName = new EditText(RecipePageActivity.this);
                                final EditText inputDesc = new EditText(RecipePageActivity.this);


                                inputName.setText(recipe_name);
                                inputDesc.setText(recipe_desc);

                                Context context = updateAlert.getContext();
                                LinearLayout layout = new LinearLayout(context);

                                layout.setOrientation(LinearLayout.VERTICAL);



                                inputName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                layout.addView(inputName);

                                inputDesc.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        |InputType.TYPE_TEXT_VARIATION_NORMAL);
                                layout.addView(inputDesc);

                                updateAlert.setView(layout);

                                updateAlert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String updatedName = inputName.getText().toString();
                                        String updatedDesc = inputDesc.getText().toString();

                                        if(updatedName.equals(recipe_name)){
                                            updatedName = null;
                                        }
                                        if(updatedDesc.equals(recipe_desc)){
                                            updatedDesc = null;
                                        }

                                        updateRecipe(recipes[position].getRecipe_id(), updatedName, updatedDesc);
                                        finish();
                                        startActivity(getIntent());

                                    }
                                });

                                updateAlert.create().show();
                                //  ((Dialog)dialog).getWindow().setLayout(600,800);
                                //dialog.cancel();
                            }
                        });
                desc.create().show();

            }
        });
        */
    }

}
