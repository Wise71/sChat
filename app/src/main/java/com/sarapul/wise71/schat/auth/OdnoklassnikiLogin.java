package com.sarapul.wise71.schat.auth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sarapul.wise71.schat.data.OkParse;
import com.sarapul.wise71.schat.models.GetTokenIdListener;

import org.json.JSONObject;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;

public class OdnoklassnikiLogin {

    private static final String APP_KEY = "CBAIKFJLEBABABABA";
    private static final String APP_ID = "1251256064";
    public static final String REDIRECT_URL = "okauth://ok1251256064";

    private static OdnoklassnikiLogin instance;

    private Odnoklassniki mOdnoklassniki;
    private GetTokenIdListener callBack;

    public static OdnoklassnikiLogin getInstance(Context context) {
        if (instance == null) {
            instance = new OdnoklassnikiLogin(context);
        }
        return instance;
    }

    private OdnoklassnikiLogin(Context context) {
        mOdnoklassniki = Odnoklassniki.createInstance(
                context, APP_ID, APP_KEY);
    }

    public void requestOk(GetTokenIdListener getTokenIdListener,
                          int requestCode, int resultCode, Intent data) {
        callBack = getTokenIdListener;
        Odnoklassniki.getInstance().onAuthActivityResult(requestCode, resultCode, data, getAuthListener());
    }

    /**
     * Creates a listener that is run on OAUTH authorization completion
     */
    @NonNull
    private OkListener getAuthListener() {
        return new OkListener() {
            @Override
            public void onSuccess(final JSONObject json) {
                requestCurrentUser();
            }

            @Override
            public void onError(String error) {
            }
        };
    }

    public Odnoklassniki getOdnoklassnikiInstance() {
        return mOdnoklassniki;
    }

    private void requestCurrentUser() {
        mOdnoklassniki.requestAsync("users.getCurrentUser", null, null, new OkListener() {
            @Override
            public void onSuccess(JSONObject json) {
                new OkParse(callBack)
                        .okJsonParse(json);
            }

            @Override
            public void onError(String error) {
            }
        });
    }

}
