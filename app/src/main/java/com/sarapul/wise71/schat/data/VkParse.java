package com.sarapul.wise71.schat.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sarapul.wise71.schat.models.GetTokenIdListener;
import com.sarapul.wise71.schat.models.vk.Response;
import com.sarapul.wise71.schat.models.vk.VkUserProfile;

import org.json.JSONObject;

public class VkParse {

    private static final String TAG = "VkStoreData";

    private Response mVkProfile;
    private String mVkId;
    private String mVkPhotoPath;
    private GetTokenIdListener callBack;

    public VkParse (GetTokenIdListener getTokenIdListener) {
        callBack = getTokenIdListener;
    }

    public void vkJsonParse(JSONObject jsonBody) {
        String vkPhotoUrl;
        Gson gson = new GsonBuilder().create();
        VkUserProfile vkResponse =
                gson.fromJson(jsonBody.toString(), VkUserProfile.class);
        mVkProfile = vkResponse.getResponse().get(0);
        vkPhotoUrl = mVkProfile.getPhotoMaxOrig();
        mVkId = mVkProfile.getId().toString() + "vk";
        mVkPhotoPath = "images/vk/" + mVkId + ".jpg";
        new ClientLoader().getTokenId(mVkId, callBack);
        new UploadPhotoTask(() ->
                new WriteData()
                        .writeToTheDatabase("vkontakte", mVkProfile), mVkPhotoPath)
                        .execute(vkPhotoUrl);

    }

}
