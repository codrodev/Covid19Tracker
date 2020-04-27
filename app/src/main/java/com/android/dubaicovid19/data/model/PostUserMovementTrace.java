package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostUserMovementTrace {
    @SerializedName("UserId")
    private String userId;

    @SerializedName("Lat")
    private String lat;

    @SerializedName("Lon")
    private String longitude;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
