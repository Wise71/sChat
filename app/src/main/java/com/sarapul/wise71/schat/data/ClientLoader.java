package com.sarapul.wise71.schat.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sarapul.wise71.schat.models.GetTokenIdListener;
import com.sarapul.wise71.schat.models.HerokuTokenId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClientLoader {
    private static final String TAG = "ClientLoader";
    private String fbToken;

    public void getTokenId(String uid, final GetTokenIdListener callback) {

        Retrofit mRetrofit = new Retrofit.Builder().baseUrl("https://schatbackend.herokuapp.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

        HerokuTokenId mHerokuTokenId = mRetrofit.create(HerokuTokenId.class);

        mHerokuTokenId.fbTokenId(uid).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                fbToken = response.body();
                Log.d(TAG, fbToken);
                callback.onGetTokenIdSuccess(fbToken);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
