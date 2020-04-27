package com.android.dubaicovid19.data.model;

import java.util.List;

public class ResponseGetTestStatistics {
    public int Code;
    public String Status;
    public List<CovidStatistics> Statistics;

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

    public List<CovidStatistics> getStatistics() {
        return Statistics;
    }

    public void setStatistics(List<CovidStatistics> statistics) {
        Statistics = statistics;
    }
}
