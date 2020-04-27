package com.android.dubaicovid19.data.model;

import java.util.List;

public class ResponseContainmentCluster {
    public int Code;
    public String Status;
    public List<ContainmentCluster> ContainmentZones;

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

    public List<com.android.dubaicovid19.data.model.ContainmentCluster> getContainmentCluster() {
        return ContainmentZones;
    }

    public void setContainmentCluster(List<com.android.dubaicovid19.data.model.ContainmentCluster> ContainmentZones) {
        this.ContainmentZones = ContainmentZones;
    }
}
