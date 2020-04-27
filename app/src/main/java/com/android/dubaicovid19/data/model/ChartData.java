package com.android.dubaicovid19.data.model;

public class ChartData {
    public int Active;
    public int Recovered;
    public int Confirmed;
    public int Deceased;
    public String Date;

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

    public int getRecovered() {
        return Recovered;
    }

    public void setRecovered(int recovered) {
        Recovered = recovered;
    }

    public int getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(int confirmed) {
        Confirmed = confirmed;
    }

    public int getDeceased() {
        return Deceased;
    }

    public void setDeceased(int deceased) {
        Deceased = deceased;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
