
package com.sarapul.wise71.schat.models.vk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("bdate")
    @Expose
    private String bdate;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("photo_50")
    @Expose
    private String photo50;
    @SerializedName("photo_100")
    @Expose
    private String photo100;
    @SerializedName("photo_200")
    @Expose
    private String photo200;
    @SerializedName("photo_max")
    @Expose
    private String photoMax;
    @SerializedName("photo_200_orig")
    @Expose
    private String photo200Orig;
    @SerializedName("photo_400_orig")
    @Expose
    private String photo400Orig;
    @SerializedName("photo_max_orig")
    @Expose
    private String photoMaxOrig;
    @SerializedName("home_town")
    @Expose
    private String homeTown;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPhoto50() {
        return photo50;
    }

    public void setPhoto50(String photo50) {
        this.photo50 = photo50;
    }

    public String getPhoto100() {
        return photo100;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public String getPhoto200() {
        return photo200;
    }

    public void setPhoto200(String photo200) {
        this.photo200 = photo200;
    }

    public String getPhotoMax() {
        return photoMax;
    }

    public void setPhotoMax(String photoMax) {
        this.photoMax = photoMax;
    }

    public String getPhoto200Orig() {
        return photo200Orig;
    }

    public void setPhoto200Orig(String photo200Orig) {
        this.photo200Orig = photo200Orig;
    }

    public String getPhoto400Orig() {
        return photo400Orig;
    }

    public void setPhoto400Orig(String photo400Orig) {
        this.photo400Orig = photo400Orig;
    }

    public String getPhotoMaxOrig() {
        return photoMaxOrig;
    }

    public void setPhotoMaxOrig(String photoMaxOrig) {
        this.photoMaxOrig = photoMaxOrig;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

}
