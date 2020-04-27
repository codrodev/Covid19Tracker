package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostUpdateCovidLog {

    @SerializedName("Name")
    private String Name;

    @SerializedName("EIdOrPassport")
    private String EIdOrPassport;

    @SerializedName("IsEid")
    private boolean IsEid;

    @SerializedName("Lat")
    private double Lat;

    @SerializedName("Lon")
    private double Lon;

    @SerializedName("TestResult")
    private String TestResult;

    @SerializedName("Mobile")
    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEIdOrPassport() {
        return EIdOrPassport;
    }

    public void setEIdOrPassport(String EIdOrPassport) {
        this.EIdOrPassport = EIdOrPassport;
    }

    public boolean isEid() {
        return IsEid;
    }

    public void setEid(boolean eid) {
        IsEid = eid;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getTestResult() {
        return TestResult;
    }

    public void setTestResult(String testResult) {
        TestResult = testResult;
    }
}
