package com.android.dubaicovid19.data.model;

import java.util.List;

public class ResponseChartData {
    public int Code;
    public String Status;
    public List<ChartData> ChartData;

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

    public List<com.android.dubaicovid19.data.model.ChartData> getChartData() {
        return ChartData;
    }

    public void setChartData(List<com.android.dubaicovid19.data.model.ChartData> chartData) {
        ChartData = chartData;
    }
}
