package com.sarapul.wise71.schat.auth;

import com.sarapul.wise71.schat.models.GetTokenIdListener;
import com.sarapul.wise71.schat.data.VkParse;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

public class VkontakteLogin {

    private GetTokenIdListener mCallBack;

    VKRequest.VKRequestListener mRequestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            new VkParse(mCallBack).vkJsonParse(response.json);
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
        }
    };

    public void requestVk(GetTokenIdListener getTokenIdListener) {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                "id,nickname,first_name,last_name,sex,bdate,city,home_town,photo_50,photo_100," +
                        "photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig"));
        request.executeWithListener(mRequestListener);
        mCallBack = getTokenIdListener;
    }

}
