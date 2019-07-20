package com.wisecity.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public static final int HTTP_STATUS_CODE_CONTINUE = 100;
    public static final int HTTP_STATUS_CODE_OK = 200;

    protected static String userName;
    private String password;
    private  String device_id_;

    EditText eTUsername;
    EditText eTPassword;
    Button btnLogin;
    TextView tVSignUpHere;

    private Token accessToken;

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
                //System.out.println("DEBUG:");
                //System.out.println("CODE:: " + response.code());
                HttpResponse httpResponse = new HttpResponse(response.code(), response.message());
                Toast.makeText(LoginActivity.this, httpResponse.toString(), Toast.LENGTH_LONG).show();

                if((httpResponse.getHttpStatus() == HTTP_STATUS_CODE_CONTINUE) || (httpResponse.getHttpStatus() == HTTP_STATUS_CODE_OK)) {
                    switchToHomeActivity(response);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error At Logging In!", Toast.LENGTH_LONG).show();
            }
        });

        JsonObject jsonObj2 = new JsonObject();
        device_id_ = ""+FirebaseInstanceId.getInstance().getToken();
        jsonObj2.addProperty("device_id", device_id_);

        Call<JsonObject> call2 = rest.likeNotification(userName, jsonObj2);
        call2.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //System.out.println("DEBUG:");
                //System.out.println("CODE:: " + response.code());
                System.out.println("XXXXXXXXXXXXXXXXX");
                HttpResponse httpResponse = new HttpResponse(response.code(), response.message());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
                Toast.makeText(LoginActivity.this, "Error At Liking!", Toast.LENGTH_LONG).show();
            }
        });


    }

    protected void switchToHomeActivity(Response<JsonObject> response) {
        accessToken = new Token(response.body().get("access_token").toString()); // Not 100% necessary written to make use of Token.java for better coding
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        homeIntent.putExtra("accessToken", accessToken.getAccessToken());
        startActivity(homeIntent);
    }

}
