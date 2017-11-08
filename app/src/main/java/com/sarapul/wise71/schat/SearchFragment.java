package com.sarapul.wise71.schat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarapul.wise71.schat.R;


public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageContactAvatar;
        private TextView mTextViewContactAvatar;
        View mView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mImageContactAvatar = (ImageView) itemView
                    .findViewById(R.id.avatarImageView);
            mTextViewContactAvatar = (TextView) itemView
                    .findViewById(R.id.avatarTextView);
        }
    }

    private FirebaseRecyclerAdapter<UserModel, ContactViewHolder>
            mFirebaseAdapter;
    private DatabaseReference usersRef;
    private RecyclerView mAvatarRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        mAvatarRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_search_recycler_view);
        mAvatarRecyclerView.setHasFixedSize(true);
        mAvatarRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), 3));
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        mFirebaseAdapter = new FirebaseRecyclerAdapter<UserModel, ContactViewHolder>(
                UserModel.class,
                R.layout.item_contact,
                ContactViewHolder.class,
                usersRef) {
            @Override
            protected void populateViewHolder(ContactViewHolder viewHolder, final UserModel userModel, int position) {

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        i.putExtra("ID", userModel.getUid());
                        startActivity(i);
                    }
                });

                Log.i(TAG, String.valueOf(viewHolder.getLayoutPosition()));

                viewHolder.mTextViewContactAvatar.setText(userModel.getNickname());
                // Отобразить фото профиля
                if (userModel.getPhotoUrl() == null) {
                    viewHolder.mImageContactAvatar
                            .setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(),
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(getActivity())
                            .load(userModel.getPhotoUrl())
                            .override(120, 120)
                            .into(viewHolder.mImageContactAvatar);
                }
            }
        };

        mAvatarRecyclerView.setAdapter(mFirebaseAdapter);

        return view;
    }

    private void activityStart() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
        Log.i(TAG, "onDestroy");
    }
}
