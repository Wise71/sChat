package com.sarapul.wise71.schat;

public class MessageModel {

    private String mUid;
    private String mText;
    private String mName;
    private String mPhotoUrl;

    public MessageModel() {
    }

    public MessageModel(String uid, String name, String text) {
        mUid = uid;
        mName = name;
//        mPhotoUrl = photoUrl;
        mText = text;
    }

    public String getId() {
        return mUid;
    }

    public void setId(String id) {
        mUid = id;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}

