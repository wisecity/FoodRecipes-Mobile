package com.wisecity.foodrecipes;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewRecipeImagesActivity extends AppCompatActivity {

    ListView listViewRecipe;

    private String recipeId;
    private Token accessToken;
    private int imageCount;
    private int finalImageCount;
    private int ingredientImageCount;
    private int stepImageCount;

    // Adjust These Wait Milliseconds according To The Running Mode
    // If DEBUG Make Them Big
    // If RUN Make Them Smaller For Efficiency
    private final int downloadWaitMilliSeconds = 7000;
    private final int notifyWaitMilliSeconds = 3000;

    ArrayList<String> urlFinalPhoto;
    ArrayList<String> urlIngredientPhoto;
    ArrayList<String> urlStepPhoto;

    ArrayList<String> urlFinalPhotoTag;
    ArrayList<String> urlIngredientPhotoTag;
    ArrayList<String> urlStepPhotoTag;

    ArrayList<String> recipeImageTagFromServer;
    ArrayList<Bitmap> recipeImageFromServer;
    PostClass postClass;
    boolean finalImagesProcessed = false;
    boolean ingredientImagesProcessed = false;
    boolean stepImagesProcessed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_images);

        listViewRecipe = findViewById(R.id.listViewRecipe);
        recipeImageTagFromServer = new ArrayList<>();
        recipeImageFromServer = new ArrayList<>();
        urlFinalPhoto = new ArrayList<>();
        urlIngredientPhoto = new ArrayList<>();
        urlStepPhoto = new ArrayList<>();
        urlFinalPhotoTag = new ArrayList<>();
        urlIngredientPhotoTag = new ArrayList<>();
        urlStepPhotoTag = new ArrayList<>();

        initializeToken();
        initializeRecipeId();
        imageCount = 0;
        finalImageCount = 0;
        ingredientImageCount = 0;
        stepImageCount = 0;
        System.out.println("RECIPE ID IS = " + recipeId);

        postClass = new PostClass(recipeImageTagFromServer, recipeImageFromServer, this);
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
                    urlFinalPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));

                    urlFinalPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    finalImageCount++;
                    imageCount++;
                    System.out.println("DEBUGFINALPHOTO " + i + " = " + urlFinalPhoto.get(i));
                    System.out.println("DEBUGFINALTAG " + i + " = " + urlFinalPhotoTag.get(i));
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
                    urlIngredientPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));

                    urlIngredientPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    ingredientImageCount++;
                    imageCount++;
                    System.out.println("DEBUGINGREDIENTPHOTO " + i + " = " + urlIngredientPhoto.get(i));
                    System.out.println("DEBUGINGREDIENTTAG " + i + " = " + urlIngredientPhotoTag.get(i));

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
                    urlStepPhoto.add(i, response.body().get(i).toString().substring(response.body().get(i).toString().indexOf("https://foodrecipesbil495.herokuapp.com/static/recipe_photos/"),response.body().get(i).toString().lastIndexOf("\"")));

                    urlStepPhotoTag.add(i, (response.body().get(i).toString().substring(response.body().get(i).toString().lastIndexOf("/")+1, response.body().get(i).toString().lastIndexOf("\""))).trim());
                    stepImageCount++;
                    imageCount++;
                    System.out.println("DEBUGSTEPPHOTO " + i + " = " + urlStepPhoto.get(i));
                    System.out.println("DEBUGSTEPTAG " + i + " = " + urlStepPhotoTag.get(i));
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
                System.out.println("Image COUNT: " + imageCount);

                new Thread() {
                    public void run() {
                        for(int i = 0; i < imageCount+2; i++) {
                            if(finalImagesProcessed == false) {
                                if(i < urlFinalPhoto.size() && i < urlFinalPhotoTag.size()) {
                                    String imageUri = urlFinalPhoto.get(i);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    recipeImageFromServer.add(tmpBitmap);
                                    recipeImageTagFromServer.add(urlFinalPhotoTag.get(i));
                                }
                                else {
                                    finalImagesProcessed = true;
                                }
                            }
                            else if(ingredientImagesProcessed == false) {
                                if((i <= urlIngredientPhoto.size() + finalImageCount) && (i <= urlIngredientPhotoTag.size() + finalImageCount)) {
                                    String imageUri = urlIngredientPhoto.get(i - finalImageCount - 1);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    recipeImageFromServer.add(tmpBitmap);
                                    recipeImageTagFromServer.add(urlIngredientPhotoTag.get(i - finalImageCount - 1));
                                }
                                else {
                                    ingredientImagesProcessed = true;
                                }
                            }
                            else if(stepImagesProcessed == false) {
                                if((i <= urlStepPhoto.size() + finalImageCount + ingredientImageCount + 1) && (i <= urlStepPhotoTag.size() + finalImageCount + ingredientImageCount + 1)) {
                                    String imageUri = urlStepPhoto.get(i - finalImageCount - ingredientImageCount - 2);
                                    //ImageView ivBasicImage = (ImageView) findViewById(R.id.ivBasicImage);
                                    Bitmap tmpBitmap = null;
                                    try {
                                        tmpBitmap = Picasso.with(getApplicationContext()).load(imageUri).get();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    recipeImageFromServer.add(tmpBitmap);
                                    recipeImageTagFromServer.add(urlStepPhotoTag.get(i - finalImageCount - ingredientImageCount - 2));
                                }
                                else {
                                    stepImagesProcessed = true;
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

    protected static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
