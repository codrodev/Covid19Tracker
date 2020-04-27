package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostGetNearByCovidCenter {
    @SerializedName("UserId")
    private int UserId;

    @SerializedName("Lat")
    private double Lat;

    @SerializedName("Lon")
    private double Lon;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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
}
