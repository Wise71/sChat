package com.sarapul.wise71.schat.auth;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sarapul.wise71.schat.data.ClientLoader;
import com.sarapul.wise71.schat.data.UploadPhotoTask;
import com.sarapul.wise71.schat.data.WriteData;
import com.sarapul.wise71.schat.models.GetTokenIdListener;

import java.util.Collections;

public class FacebookLogin {
    private GetTokenIdListener mTokenIdListener;
    private CallbackManager mCallBackManager;
    private Profile mFacebookProfile;
    private String mFacebookId;
    private String mFacebookPhotoPath;

    public FacebookLogin(GetTokenIdListener getTokenIdListener) {
        mTokenIdListener = getTokenIdListener;
        mCallBackManager = CallbackManager.Factory.create();
        FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override public void onSuccess(LoginResult loginResult) {
                writeDatabase();
            }

            @Override public void onCancel() {

            }

            @Override public void onError(FacebookException e) {

            }
        };
        LoginManager.getInstance().registerCallback(mCallBackManager, mCallBack);
    }

    @NonNull @CheckResult
    public CallbackManager getCallbackManager() {
        return mCallBackManager;
    }

    public void performSignIn(Activity activity) {
        LoginManager.getInstance()
                .logInWithReadPermissions(activity,
                        Collections.singletonList("public_profile"));
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data, ProgressBar progressBar) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public void performSignOut() {
        LoginManager.getInstance().logOut();
    }

    private void writeDatabase() {
        String vkPhotoUrl;
        mFacebookProfile = Profile.getCurrentProfile();
        vkPhotoUrl = mFacebookProfile.getProfilePictureUri(200, 400).toString();
        mFacebookId = mFacebookProfile.getId() + "fb";
        mFacebookPhotoPath = "images/facebook/" + mFacebookId + ".jpg";
        new ClientLoader().getTokenId(mFacebookId, mTokenIdListener);
        new UploadPhotoTask(() ->
                new WriteData()
                        .writeToTheDatabase("facebook", mFacebookProfile), mFacebookPhotoPath)
                .execute(vkPhotoUrl);
    }
}
