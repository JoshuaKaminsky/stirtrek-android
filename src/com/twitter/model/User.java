package com.twitter.model;

import com.google.gson.annotations.SerializedName;

public class User {
    
    @SerializedName("name")
    public String name;

    @SerializedName("screen_name")
    public String handle;
}
