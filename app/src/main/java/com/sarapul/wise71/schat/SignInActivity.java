package com.sarapul.wise71.schat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarapul.wise71.schat.R;
import com.sarapul.wise71.schat.auth.OdnoklassnikiLogin;
import com.sarapul.wise71.schat.auth.VkontakteLogin;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

import static com.sarapul.wise71.schat.auth.OdnoklassnikiLogin.REDIRECT_URL;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener {

  private static final String TAG = "SignInActivity";

    private ProgressBar mProgressBar;
    private OdnoklassnikiLogin mOdnoklassnikiLogin;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    CallbackManager mFacebookCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressBar = findViewById(R.id.progressBar);

        mOdnoklassnikiLogin = OdnoklassnikiLogin.getInstance(this.getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

        // Assign fields
      Button mSignInVkButton = findViewById(R.id.sign_in_button_vk);
      Button mTestButton = findViewById(R.id.sign_in_test_button);
      Button mSignInOkButton = findViewById(R.id.sign_in_button_ok);
      LoginButton mFacebookButton = findViewById(R.id.facebook_login_button);

        mFacebookButton.setReadPermissions("email");
        mFacebookButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "onError");
            }
        });

        // Set click listeners
        mSignInOkButton.setOnClickListener(this);
        mSignInVkButton.setOnClickListener(this);
        mTestButton.setOnClickListener(this);

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_ok:
                mOdnoklassnikiLogin.getOdnoklassnikiInstance()
                        .requestAuthorization(this,
                        REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS);
                break;
            case R.id.sign_in_button_vk:
                VKSdk.login(this, VKScope.PHOTOS);
                break;
            case R.id.sign_in_test_button:
                break;
            default:
                break;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        mProgressBar.setVisibility(ProgressBar.GONE);
        finish();
    }

    public void signInWithFirebase(String uId) {

        mFirebaseAuth.signInWithCustomToken(uId)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        startMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                new VkontakteLogin().requestVk(uId -> signInWithFirebase(uId) );
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
        }

        if (Odnoklassniki.getInstance().isActivityRequestOAuth(requestCode)) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            mOdnoklassnikiLogin.requestOk(
                    uId -> signInWithFirebase(uId), requestCode, resultCode, data);
        }

        if (mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
