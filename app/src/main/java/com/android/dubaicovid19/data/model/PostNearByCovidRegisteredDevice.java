package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostNearByCovidRegisteredDevice {

    @SerializedName("listUUID")
    private String[] listUUID;

    public String[] getListUUID ()
    {
        return listUUID;
    }

    public void setListUUID (String[] listUUID)
    {
        this.listUUID = listUUID;
    }

}
