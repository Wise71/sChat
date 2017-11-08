package com.sarapul.wise71.schat.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sarapul.wise71.schat.ClientLoader;
import com.sarapul.wise71.schat.models.GetTokenIdListener;
import com.sarapul.wise71.schat.models.vk.Response;
import com.sarapul.wise71.schat.models.vk.VkUserProfile;

import org.json.JSONObject;

public class VkParse {

    private static final String TAG = "VkStoreData";

    private Response vkProfile;
    private String vkontakteId;
    private String mVkId;
    private String vkPhotoPath;
    private DatabaseReference mFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();
    private GetTokenIdListener callBack;

    public VkParse (GetTokenIdListener getTokenIdListener) {
        callBack = getTokenIdListener;
    }

    public void vkJsonParse(JSONObject jsonBody) {
        String vkPhotoUrl;
        Gson gson = new GsonBuilder().create();
        VkUserProfile vkResponse =
                gson.fromJson(jsonBody.toString(), VkUserProfile.class);
        vkProfile = vkResponse.getResponse().get(0);
        vkPhotoUrl = vkProfile.getPhotoMaxOrig();
        vkontakteId = vkProfile.getId().toString();
        mVkId = vkProfile.getId().toString() + "vk";
        vkPhotoPath = "images/vk/" + mVkId + ".jpg";
        new ClientLoader().getTokenId(vkontakteId + "vk", callBack);
        new UploadPhotoTask(() ->
                new WriteData()
                        .writeToTheDatabase("vkontakte", vkProfile), vkPhotoPath)
                        .execute(vkPhotoUrl);

    }

}
