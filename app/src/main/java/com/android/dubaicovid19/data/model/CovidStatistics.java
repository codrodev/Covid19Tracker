package com.android.dubaicovid19.data.model;

public class CovidStatistics {
    public String Date;
    public int TotalIndividualsTested;
    public int TotalSamplesTested;
    public int TotalPositiveCases;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getTotalIndividualsTested() {
        return TotalIndividualsTested;
    }

    public void setTotalIndividualsTested(int totalIndividualsTested) {
        TotalIndividualsTested = totalIndividualsTested;
    }

    public int getTotalSamplesTested() {
        return TotalSamplesTested;
    }

    public void setTotalSamplesTested(int totalSamplesTested) {
        TotalSamplesTested = totalSamplesTested;
    }

    public int getTotalPositiveCases() {
        return TotalPositiveCases;
    }

    public void setTotalPositiveCases(int totalPositiveCases) {
        TotalPositiveCases = totalPositiveCases;
    }
}
