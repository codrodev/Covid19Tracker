package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostRegisterUserModel {
    @SerializedName("Username")
    private String username;

    @SerializedName("EIdOrPassport")
    private String eIdOrPassport;

    @SerializedName("IsEId")
    private boolean IsEId;

    @SerializedName("Mobile")
    private long mobile;

    @SerializedName("Email")
    private String email;

    @SerializedName("GenderCode")
    private String genderCode;

    @SerializedName("Age")
    private int age;

    @SerializedName("CountryCode")
    private String nationalityCode;

    @SerializedName("Lat")
    private String lat;

    @SerializedName("Lon")
    private String longitude;

    @SerializedName("CovidInfoCode")
    private String covidInfoCode;

    @SerializedName("UUID")
    private String uniqueID;

    /*@SerializedName("BLE_Address")
    private String BLE_Address;*/

    /*public String getBLE_Address() {
        return BLE_Address;
    }

    public void setBLE_Address(String BLE_Address) {
        this.BLE_Address = BLE_Address;
    }*/

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String geteIdOrPassport() {
        return eIdOrPassport;
    }

    public void seteIdOrPassport(String eIdOrPassport) {
        this.eIdOrPassport = eIdOrPassport;
    }

    public boolean isEId() {
        return IsEId;
    }

    public void setEId(boolean EId) {
        IsEId = EId;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCovidInfoCode() {
        return covidInfoCode;
    }

    public void setCovidInfoCode(String covidInfoCode) {
        this.covidInfoCode = covidInfoCode;
    }
}
