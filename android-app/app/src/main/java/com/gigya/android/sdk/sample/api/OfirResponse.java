package com.gigya.android.sdk.sample.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfirResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("responseCode")
    @Expose
    private Integer id;

    public String getMessage() {
        return message;
    }

    public Integer getId() {
        return id;
    }
}
