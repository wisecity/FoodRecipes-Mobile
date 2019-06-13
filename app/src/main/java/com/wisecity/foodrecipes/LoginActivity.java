package com.wisecity.foodrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public static final int HTTP_STATUS_CODE_CONTINUE = 100;

    private String userName;
    private String password;

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = eTUsername.getText().toString();
                password = eTPassword.getText().toString();
                sendLoginData();
            }
        });

        tVSignUpHere = findViewById(R.id.tVLoginHere);
        tVSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    protected void sendLoginData() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", userName);
        jsonObj.addProperty("password", password);

        Call<JsonObject> call = rest.sendLogin(jsonObj); // Sends login data to heroku app with the JSON object that has been created.
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HttpResponse httpResponse = new HttpResponse(response.body().get("httpStatus").getAsInt(), response.body().get("httpMessage").toString());
                Toast.makeText(LoginActivity.this, httpResponse.toString(), Toast.LENGTH_LONG).show();

                if(httpResponse.getHttpStatus() == HTTP_STATUS_CODE_CONTINUE) { 
                    switchToHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error At Logging In!", Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void switchToHomeActivity() {
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
    }

}
