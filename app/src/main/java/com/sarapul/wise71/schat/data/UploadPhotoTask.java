package com.sarapul.wise71.schat.data;

import android.os.AsyncTask;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarapul.wise71.schat.models.WriteDatabaseListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UploadPhotoTask extends AsyncTask<String, Void, Void> {

    public UploadPhotoTask(WriteDatabaseListener writeDatabaseListener,
                           String photoPath) {
        callBack = writeDatabaseListener;
        this.photoPath = photoPath;
    }

    private FirebaseStorage mFirebaseStorage =
            FirebaseStorage.getInstance();
    private StorageReference mFirebaseStorageReference =
            mFirebaseStorage.getReference();
    private String photoPath;
    private WriteDatabaseListener callBack;

    @Override
    protected Void doInBackground(String... photoUrl) {

        InputStream stream = null;
        try {
            stream = new URL(photoUrl[0]).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference riversRef =
                mFirebaseStorageReference.child(photoPath);

        UploadTask uploadTask = riversRef.putStream(stream);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            callBack.onWriteDatabase();
        });
        return null;
    }

}
