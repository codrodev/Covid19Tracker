package com.android.dubaicovid19.data.model;

public class ResponseGetLiveUpdates {

    public int Code;
    public String Status;
    public Statistics Statistics;

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

    public com.android.dubaicovid19.data.model.Statistics getStatistics() {
        return Statistics;
    }

    public void setStatistics(com.android.dubaicovid19.data.model.Statistics statistics) {
        Statistics = statistics;
    }
}
