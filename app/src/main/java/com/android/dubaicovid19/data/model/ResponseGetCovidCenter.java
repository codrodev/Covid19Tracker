package com.android.dubaicovid19.data.model;

public class ResponseGetCovidCenter {
    public String Code;
    public String Status;
    public CovidCenters[] CovidCenters;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public CovidCenters[] getLstCovidCenters() {
        return CovidCenters;
    }

    public void setLstCovidCenters(CovidCenters[] CovidCenters) {
        this.CovidCenters = CovidCenters;
    }
}
