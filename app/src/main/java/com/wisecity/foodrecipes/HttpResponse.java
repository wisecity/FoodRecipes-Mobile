package com.wisecity.foodrecipes;

import com.google.gson.annotations.SerializedName;

public class HttpResponse {

    @SerializedName("httpstatus")
    private int httpStatus;
    @SerializedName("httpmessage")
    private String httpMessage;

    public HttpResponse(int httpStatus, String httpMmessage) {
        this.httpStatus = httpStatus;
        this.httpMessage = httpMmessage;
    }

    public int getHttpStatusStatus() {
        return httpStatus;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public String toString(){
        return "Status Code : " + httpStatus + "Message : " + httpMessage;
    }
}
