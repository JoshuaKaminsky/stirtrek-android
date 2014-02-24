package com.twitter.model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    
    @SerializedName("token_type")
    public String tokenType;
    
    @SerializedName("access_token")
    public String accessToken;
}
