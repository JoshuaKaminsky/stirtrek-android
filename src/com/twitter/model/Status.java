package com.twitter.model;


import com.google.gson.annotations.SerializedName;

public class Status {
    
    @SerializedName("created_at")
    public String createdAt;
    
    public User user;
    
    public String text;
    
}
