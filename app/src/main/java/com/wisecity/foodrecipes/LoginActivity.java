package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText eTUsername;
    EditText eTPassword;
    Button btnLogin;
    TextView tVSignUpHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTUsername = findViewById(R.id.eTUsername);
        eTPassword = findViewById(R.id.eTPassword);
        btnLogin = findViewById(R.id.btnRegister);
        tVSignUpHere = findViewById(R.id.tVLoginHere);

        tVSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

}
