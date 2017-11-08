package com.sarapul.wise71.schat.models;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HerokuTokenId {
    @POST("/generate")
    Call<String> fbTokenId (@Query("uid")String uid);
}
