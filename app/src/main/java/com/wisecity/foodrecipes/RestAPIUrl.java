package com.wisecity.foodrecipes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAPIUrl {

    public Retrofit createRetrofitFromUrl() {
        Retrofit retrofit =  new Retrofit.Builder().baseUrl("https://foodrecipe495.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

}
