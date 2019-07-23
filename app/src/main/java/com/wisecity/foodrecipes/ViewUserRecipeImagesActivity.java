package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewUserRecipeImagesActivity extends AppCompatActivity {

    ListView listViewRecipe;

    private String recipeId;
    private Token accessToken;
    private int userImageCount;
    private int userFinalImageCount;
    private int userIngredientImageCount;
    private int userStepImageCount;

    // Adjust These Wait Milliseconds according To The Running Mode
    // If DEBUG Make Them Big
    // If RUN Make Them Smaller For Efficiency
    private final int downloadWaitMilliSeconds = 7000;
    private final int notifyWaitMilliSeconds = 3000;

    ArrayList<String> urlUserFinalPhoto;
    ArrayList<String> urlUserIngredientPhoto;
    ArrayList<String> urlUserStepPhoto;

    ArrayList<String> urlUserFinalPhotoTag;
    ArrayList<String> urlUserIngredientPhotoTag;
    ArrayList<String> urlUserStepPhotoTag;

    ArrayList<String> userRecipeImageTagFromServer;
    ArrayList<Bitmap> userRecipeImageFromServer;
    PostClass postClass;

    boolean userFinalImagesProcessed = false;
    boolean userIngredientImagesProcessed = false;
    boolean userStepImagesProcessed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_recipe_images);

        initializeToken();
        initializeRecipeId();
        userImageCount = 0;
        userFinalImageCount = 0;
        userIngredientImageCount = 0;
        userStepImageCount = 0;

        urlUserFinalPhoto = new ArrayList<>();
        urlUserIngredientPhoto = new ArrayList<>();
        urlUserStepPhoto = new ArrayList<>();
        urlUserFinalPhotoTag = new ArrayList<>();
        urlUserIngredientPhotoTag = new ArrayList<>();
        urlUserStepPhotoTag = new ArrayList<>();

        listViewRecipe = findViewById(R.id.listViewRecipe);
        userRecipeImageTagFromServer = new ArrayList<>();
        userRecipeImageFromServer = new ArrayList<>();

        postClass = new PostClass(userRecipeImageTagFromServer, userRecipeImageFromServer, this);
        listViewRecipe.setAdapter(postClass);

        download();
        processesAfterDownload();
    }

    private void download() {

        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();
        RestAPI rest = retrofit.create(RestAPI.class);
        Call<JsonArray> callForFinalPhoto = rest.getRecipeFinalPhoto(recipeId);
        callForFinalPhoto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                for(int i = 0; i < response.body().size(); i++) {
                    urlUserFinalPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));
                    urlUserFinalPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    userFinalImageCount++;
                    userImageCount++;
                    System.out.println("DEBUGFINALPHOTO " + i + " = " + urlUserFinalPhoto.get(i));
                    System.out.println("DEBUGFINALTAG " + i + " = " + urlUserFinalPhotoTag.get(i));
                }
                Toast.makeText(getApplicationContext(), "Final Image Has Been Downloaded", Toast.LENGTH_SHORT).show();
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
                for(int i = 0; i < response.body().size(); i++) {
                    urlUserIngredientPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));
                    urlUserIngredientPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    userIngredientImageCount++;
                    userImageCount++;
                    System.out.println("DEBUGINGREDIENTPHOTO " + i + " = " + urlUserIngredientPhoto.get(i));
                    System.out.println("DEBUGINGREDIENTTAG " + i + " = " + urlUserIngredientPhotoTag.get(i));

                }
                Toast.makeText(getApplicationContext(), "Ingredient Image Has Been Downloaded", Toast.LENGTH_SHORT).show();
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
                for(int i = 0; i < response.body().size(); i++) {
                    urlUserStepPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));
                    urlUserStepPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    userStepImageCount++;
                    userImageCount++;
                    System.out.println("DEBUGSTEPPHOTO " + i + " = " + urlUserStepPhoto.get(i));
                    System.out.println("DEBUGSTEPTAG " + i + " = " + urlUserStepPhotoTag.get(i));
                }
                Toast.makeText(getApplicationContext(), "Recipe Step Image Has Been Downloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error At Downloading Recipe Step Image From The Server!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // A Wait Is Needed For The Enqueued Processes To Finish.
    // Arrange The downloadWaitMilliSeconds If Needed (For More Or Less Time).
    private void processesAfterDownload() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after downloadWaitMilliSeconds seconds
                System.out.println("Image COUNT: " + userImageCount);


                new Thread() {
                    public void run() {
                        for(int i = 0; i < userImageCount+2; i++) {
                            if(userFinalImagesProcessed == false) {
                                if(i < urlUserFinalPhoto.size() && i < urlUserFinalPhotoTag.size()) {
                                    String imageUri = urlUserFinalPhoto.get(i);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    userRecipeImageFromServer.add(tmpBitmap);
                                    userRecipeImageTagFromServer.add(urlUserFinalPhotoTag.get(i));
                                }
                                else {
                                    userFinalImagesProcessed = true;
                                }
                            }
                            else if(userIngredientImagesProcessed == false) {
                                if((i <= urlUserIngredientPhoto.size() + userFinalImageCount)&& (i <= urlUserIngredientPhotoTag.size() + userFinalImageCount)) {
                                    String imageUri = urlUserIngredientPhoto.get(i - userFinalImageCount - 1);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    userRecipeImageFromServer.add(tmpBitmap);
                                    userRecipeImageTagFromServer.add(urlUserIngredientPhotoTag.get(i - userFinalImageCount - 1));
                                }
                                else {
                                    userIngredientImagesProcessed = true;
                                }
                            }
                            else if(userStepImagesProcessed == false) {
                                if((i <= urlUserStepPhoto.size() + userFinalImageCount + userIngredientImageCount + 1)&& (i <= urlUserStepPhotoTag.size() + userFinalImageCount + userIngredientImageCount + 1)) {
                                    String imageUri = urlUserStepPhoto.get(i - userFinalImageCount - userIngredientImageCount - 2);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    userRecipeImageFromServer.add(tmpBitmap);
                                    userRecipeImageTagFromServer.add(urlUserStepPhotoTag.get(i - userFinalImageCount - userIngredientImageCount - 2));
                                }
                                else {
                                    userStepImagesProcessed = true;
                                    break;
                                }
                            }
                        }
                    }
                }.start();
                Handler handlerForNotification = new Handler();
                handlerForNotification.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        postClass.notifyDataSetChanged();
                    }
                }, notifyWaitMilliSeconds);
            }
        }, downloadWaitMilliSeconds);
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

    private void switchToViewUserRecipeImagesActivity(Token accessToken) {
        Intent viewUserRecipeImagesIntent = new Intent(getApplicationContext(), ViewRecipeImagesActivity.class);
        viewUserRecipeImagesIntent.putExtra("accessToken", accessToken.getAccessToken());
        viewUserRecipeImagesIntent.putExtra("Recipe Id", recipeId);
        startActivity(viewUserRecipeImagesIntent);
    }

    protected void viewRecipeImages(View view) {
        switchToViewUserRecipeImagesActivity(accessToken);
    }
}
