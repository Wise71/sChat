package com.sarapul.wise71.schat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sarapul.wise71.schat.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }

    private static final String TAG = "ChatFragment";
    public static final String MESSAGES_CHILD = "messages";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";
    private SharedPreferences mSharedPreferences;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mFirebaseStorageReference;
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>
            mFirebaseAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseStorageReference = mFirebaseStorage.getReference();

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) v.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);


        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(
                MessageModel.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              MessageModel messageModel, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(messageModel.getText());
                viewHolder.messengerTextView.setText(messageModel.getName());

                if (messageModel.getPhotoUrl() == null) {
//                    viewHolder.messengerImageView
//                            .setImageDrawable(ContextCompat
//                                    .getDrawable(getActivity(),
//                                            R.drawable.ic_account_circle_black_36dp));
                   StorageReference storageReference =
                           mFirebaseStorageReference.child("images/apple.jpg");
                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(viewHolder.messengerImageView);
                } else {
                    Glide.with(getActivity())
                            .load(messageModel.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) v.findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) v.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String photoUrl;
                if (mFirebaseUser.getPhotoUrl() == null) {
                    StorageReference pathReference
                            = mFirebaseStorageReference.child("images/apple.jpg");
//                    StorageReference httpsReference =
//                            mFirebaseStorage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
                    String urlRef = pathReference.getDownloadUrl().toString();
                    Log.d(TAG, urlRef);
                    photoUrl = urlRef;
                } else {
                    photoUrl = mFirebaseUser.getPhotoUrl().toString();
                }
                MessageModel messageModel = new MessageModel(
                        mFirebaseUser.getUid(),
                        mFirebaseUser.getDisplayName(),
//                        photoUrl,
                        mMessageEditText.getText().toString()
                );
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                        .push().setValue(messageModel);
                mMessageEditText.setText("");
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}