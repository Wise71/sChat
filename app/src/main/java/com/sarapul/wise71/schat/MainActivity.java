package com.sarapul.wise71.schat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarapul.wise71.schat.R;
import com.sarapul.wise71.schat.auth.OdnoklassnikiLogin;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private OdnoklassnikiLogin mOdnoklassnikiLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOdnoklassnikiLogin = OdnoklassnikiLogin.getInstance(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            startChatFragment();
        } else {
            startSignInActivity();
        }

    }

    private void startChatFragment() {
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_button_chat:
                    fragment = new ChatFragment();
                    fragment.setRetainInstance(true);
                    break;
                case R.id.nav_button_dialogs:
                    fragment = new DialogFragment();
                    fragment.setRetainInstance(true);
                    break;
                case R.id.nav_button_search:
                    fragment = new SearchFragment();
                    fragment.setRetainInstance(true);
                    break;
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();

            return true;
        });
        fragment = new ChatFragment();
        fragment.setRetainInstance(true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }


    private void startSignInActivity() {
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d(TAG, "onCreateOptionsMenu: ok");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                VKSdk.logout();
                mOdnoklassnikiLogin.getOdnoklassnikiInstance().clearTokens();
                mFirebaseAuth.signOut();
                startSignInActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}