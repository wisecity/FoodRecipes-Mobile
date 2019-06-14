package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Token accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toast.makeText(HomeActivity.this, "Welcome!", Toast.LENGTH_LONG).show();

        accessToken = new Token(getAccessTokenFromLogin());

    }

    protected String getAccessTokenFromLogin() {
        Intent fromLoginIntent = getIntent();
        String accessToken = fromLoginIntent.getStringExtra("accessToken");
        System.out.println("DEBUG, TOKEN IS:" + accessToken);
        return accessToken;
    }

}
