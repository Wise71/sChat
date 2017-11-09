package com.sarapul.wise71.schat.data;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarapul.wise71.schat.UserModel;
import com.sarapul.wise71.schat.models.ok.OkUserProfile;
import com.sarapul.wise71.schat.models.vk.Response;

public class WriteData {

    private DatabaseReference mFirebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference();
    private String socialId;
    private String nickname;
    private String firstName;
    private String lastName;
    private String birthday;
    private String photoPath;
    private String gender;
    private String city;

    public void writeToTheDatabase(String socialName, Object profile) {

        switch (socialName) {
            case "vkontakte" :
                Response vkProfile = (Response) profile;
                socialId = vkProfile.getId().toString() + "vk";
                nickname = vkProfile.getNickname();
                firstName = vkProfile.getFirstName();
                lastName = vkProfile.getLastName();
                birthday = vkProfile.getBdate();
                photoPath = "images/vk/" + socialId + ".jpg";
                gender = vkProfile.getSex().toString();
                if (vkProfile.getCity() != null) {
                    city = vkProfile.getCity().getTitle();
                } else {
                    city = vkProfile.getHomeTown();
                }
                break;

            case "odnoklassniki" :
                OkUserProfile okProfile = (OkUserProfile) profile;
                socialId = okProfile.getUid() + "ok";
                nickname = okProfile.getName();
                firstName = okProfile.getFirstName();
                lastName = okProfile.getLastName();
                birthday = okProfile.getBirthday();
                photoPath = "images/ok/" + socialId + ".jpg";
                gender = okProfile.getGender();
                city = okProfile.getLocation().getCity();
                break;

            case "facebook" :
                Profile facebookProfile = (Profile) profile;
                socialId = facebookProfile.getId() + "facebook";
                nickname = facebookProfile.getName();
                firstName = facebookProfile.getFirstName();
                lastName = facebookProfile.getLastName();
                birthday = null;
                photoPath = "images/facebook/" + socialId + ".jpg";
                gender = null;
                city = null;
                break;

            default:
                break;

        }

        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                String uuId = UUID.randomUUID().toString();
                UserModel getUserAccount = dataSnapshot
                        .child("users")
                        .child(socialName)
                        .child(socialId)
                        .getValue(UserModel.class);
                if (getUserAccount == null) {

                    UserModel userModel = new UserModel(
                            nickname,
                            firstName,
                            lastName,
                            birthday,
                            photoPath,
                            gender,
                            city
                    );
                    mFirebaseDatabaseReference
                            .child("users")
                            .child(socialName)
                            .child(socialId)
                            .setValue(userModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
