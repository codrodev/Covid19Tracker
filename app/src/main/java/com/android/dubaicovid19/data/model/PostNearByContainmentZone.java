package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostNearByContainmentZone {
    @SerializedName("Lat")
    private double Lat;

    @SerializedName("Lon")
    private double Lon;

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
