package com.android.dubaicovid19.data.model;

import java.util.List;

public class ResponseNeighbourhoodInfectedUserCount {

    public int Code;
    public String Status;
    public int NearByInfectedUserCount;
    public List<NearByInfectedUser> NearByInfectedUser;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getNearByInfectedUserCount() {
        return NearByInfectedUserCount;
    }

    public void setNearByInfectedUserCount(int nearByInfectedUserCount) {
        NearByInfectedUserCount = nearByInfectedUserCount;
    }

    public List<NearByInfectedUser> getNearByInfectedUser() {
        return NearByInfectedUser;
    }

    public void setNearByInfectedUser(List<NearByInfectedUser> NearByInfectedUser) {
        this.NearByInfectedUser = NearByInfectedUser;
    }
}
