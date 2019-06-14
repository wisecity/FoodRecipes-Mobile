package com.wisecity.foodrecipes;

import com.google.gson.annotations.SerializedName;

public class HttpResponse {

    @SerializedName("status_code")
    private int httpStatus;
    @SerializedName("message")
    private String httpMessage;


    public HttpResponse(int httpStatus, String httpMessage) {
        this.httpStatus = httpStatus;
        this.httpMessage = httpMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public String toString(){
        return "Status Code : " + httpStatus + " Message : " + httpMessage;
    }
}
