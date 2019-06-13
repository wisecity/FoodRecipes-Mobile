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

public class RegisterActivity extends AppCompatActivity {

    private String userName;
    private String password;

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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = eTUsername.getText().toString();
                password = eTPassword.getText().toString();
                sendRegisterData();
            }
        });

        tVLoginHere = findViewById(R.id.tVLoginHere);
        tVLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    protected void sendRegisterData() {
        RestAPIUrl url = new RestAPIUrl();
        Retrofit retrofit = url.createRetrofitFromUrl();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", userName);
        jsonObj.addProperty("password", password);
        Call<JsonObject> call = rest.saveRegister(jsonObj); // Sends register data to heroku app with the JSON object created.

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HttpResponse httpResponse = new HttpResponse(response.body().get("httpStatus").getAsInt(), response.body().get("httpMessage").toString());
                Toast.makeText(RegisterActivity.this, httpResponse.toString(), Toast.LENGTH_LONG).show();

                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error At Registration!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
