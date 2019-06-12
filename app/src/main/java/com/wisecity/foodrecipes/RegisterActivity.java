package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    EditText eTUsername;
    EditText eTPassword;
    EditText eTConfirmPassword;
    Button btnRegister;
    TextView tVLoginHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eTUsername = findViewById(R.id.eTUsername);
        eTPassword = findViewById(R.id.eTPassword);
        eTConfirmPassword = findViewById(R.id.eTConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tVLoginHere = findViewById(R.id.tVLoginHere);

        tVLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
