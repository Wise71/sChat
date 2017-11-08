package com.sarapul.wise71.schat;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarapul.wise71.schat.R;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private DatabaseReference user;
    private ImageView mImageView;
    private TextView mNicknameTextView;
    private TextView mEmailTextView;
    private String mPhotoUrl;
    private String mNickname;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = (ImageView) findViewById(R.id.profileImageView);
        mNicknameTextView = (TextView) findViewById(R.id.profileNickname);
        mEmailTextView = (TextView) findViewById(R.id.profileEmail);

        String userId = getIntent().getExtras().getString("ID", "defaultValue");

        FirebaseDatabase.getInstance().getReference().child("users/" + userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mPhotoUrl = (String) dataSnapshot.child("photoUrl").getValue();
                        mNickname = (String) dataSnapshot.child("name").getValue();
                        mEmail = (String) dataSnapshot.child("email").getValue();
                        mNicknameTextView.setText(mNickname);
                        mEmailTextView.setText(mEmail);
                        if (mPhotoUrl == null) {
                            mImageView
                                    .setImageDrawable(ContextCompat
                                            .getDrawable(ProfileActivity.this,
                                                    R.drawable.ic_account_circle_black_36dp));
                        } else {
                            Glide.with(ProfileActivity.this)
                                    .load(mPhotoUrl)
                                    .override(600, 600)
//                                    .centerCrop()
//                                    .fitCenter()
                                    .into(mImageView);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}
