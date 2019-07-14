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

public class ViewRecipeActivity extends AppCompatActivity {
    ImageButton iBHome;
    private Token accessToken;

    private String recipeName;
    private String recipeContents;
    private String recipeDetails;

    EditText eTRecipeName;
    EditText eTRecipeContents;
    EditText eTRecipeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        initializeToken();
        iBHome = findViewById(R.id.iBHome);
        iBHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHomeActivity(accessToken);
            }
        });

        eTRecipeName = findViewById(R.id.eTRecipeName);
        eTRecipeContents = findViewById(R.id.eTRecipeContents);
        eTRecipeDetails = findViewById(R.id.eTRecipeDetails);

        getInitialRecipeInfo();

        eTRecipeName.setEnabled(false);
        eTRecipeContents.setEnabled(false);
        eTRecipeDetails.setEnabled(false);
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
        Intent userHomeIntent = new Intent(ViewRecipeActivity.this, HomeActivity.class);
        userHomeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(userHomeIntent);
    }

    protected void getInitialRecipeInfo() {
        Bundle bundleViewRecipe = getIntent().getExtras();
        recipeName = bundleViewRecipe.getString("Recipe Name");
        recipeContents = bundleViewRecipe.getString("Recipe Contents");
        recipeDetails = bundleViewRecipe.getString("Recipe Details");
        eTRecipeName.setText(recipeName);
        eTRecipeContents.setText(recipeContents);
        eTRecipeDetails.setText(recipeDetails);
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


}
