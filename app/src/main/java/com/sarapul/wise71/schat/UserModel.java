/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sarapul.wise71.schat;

public class UserModel {

    private String mUid;
    private String mNickname;
    private String mFirstName;
    private String mLastName;
    private String mDayOfBirth;
    private String mGender;
    private String mCity;
    private String mEmail;
    private String mPhotoUrl;
    private String mVkontakteId;
    private String mOdnoklassnikiId;
    private String mFacebookId;

    public UserModel() {
    }

    public UserModel(String nickname,
                     String firstName, String lastName,
                     String dayOfBirth, String photoUrl,
                     String gender, String city) {

        mNickname = nickname;
        mFirstName = firstName;
        mLastName = lastName;
        mDayOfBirth = dayOfBirth;
        mPhotoUrl = photoUrl;
        mGender = gender;
        mCity = city;
    }

    public UserModel(String nickname, String firstName,
                     String lastName, String photoUrl) {
        mNickname = nickname;
        mFirstName = firstName;
        mLastName = lastName;
        mPhotoUrl = photoUrl;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getDayOfBirth() {
        return mDayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        mDayOfBirth = dayOfBirth;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public String getVkontakteId() {
        return mVkontakteId;
    }

    public void setVkontakteId(String vkontakteId) {
        mVkontakteId = vkontakteId;
    }

    public String getOdnoklassnikiId() {
        return mOdnoklassnikiId;
    }

    public void setOdnoklassnikiId(String odnoklassnikiId) {
        mOdnoklassnikiId = odnoklassnikiId;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }
}
