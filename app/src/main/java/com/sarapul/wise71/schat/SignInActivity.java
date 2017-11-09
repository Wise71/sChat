package com.sarapul.wise71.schat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarapul.wise71.schat.auth.FacebookLogin;
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

    private Button mSignInVkButton, mTestButton, mSignInOkButton, mFacebookButton;
    private ProgressBar mProgressBar;
    private OdnoklassnikiLogin mOdnoklassnikiLogin;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FacebookLogin mFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressBar = findViewById(R.id.progressBar);

        mOdnoklassnikiLogin = OdnoklassnikiLogin
                .getInstance(this.getApplicationContext());

        // Assign fields
        mSignInVkButton = findViewById(R.id.sign_in_button_vk);
        mTestButton = findViewById(R.id.sign_in_test_button);
        mSignInOkButton = findViewById(R.id.sign_in_button_ok);
        mFacebookButton = findViewById(R.id.facebook_login_button);

        // Set click listeners
        mSignInOkButton.setOnClickListener(this);
        mSignInVkButton.setOnClickListener(this);
        mTestButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);

        mFacebook = new FacebookLogin(this::signInWithFirebase);

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
            case R.id.facebook_login_button:
                mFacebook.performSignIn(this);
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

        if (!VKSdk.onActivityResult(requestCode, resultCode, data,
                new VKCallback<VKAccessToken>() {
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
                    this::signInWithFirebase, requestCode, resultCode, data);
        }

        mFacebook.onActivityResult(requestCode, resultCode, data, mProgressBar);
    }

}
