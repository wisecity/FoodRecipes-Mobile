package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UserProfileActivity extends AppCompatActivity {

    private Token accessToken;
    ImageButton iBHome;

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



}
