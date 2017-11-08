package com.sarapul.wise71.schat.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sarapul.wise71.schat.ClientLoader;
import com.sarapul.wise71.schat.models.GetTokenIdListener;
import com.sarapul.wise71.schat.models.ok.OkUserProfile;

import org.json.JSONObject;

public class OkParse {

    // Instance variables
    private OkUserProfile okProfile;
    private GetTokenIdListener callBack;

    private DatabaseReference mFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();

    public OkParse(GetTokenIdListener getTokenIdListener) {
        callBack = getTokenIdListener;
    }

    public void okJsonParse(JSONObject json) {
        String okPhotoUrl;
        Gson gson = new GsonBuilder().create();
        okProfile = gson.fromJson(json.toString(), OkUserProfile.class);
        okPhotoUrl = okProfile.getPic2();
        String okId = okProfile.getUid() + "ok";
        String okPhotoPath = "images/ok/" + okId + ".jpg";
        new ClientLoader().getTokenId(okId, callBack);
        new UploadPhotoTask(() ->
                new WriteData()
                        .writeToTheDatabase("odnoklassniki", okProfile), okPhotoPath)
                .execute(okPhotoUrl);
    }

}
