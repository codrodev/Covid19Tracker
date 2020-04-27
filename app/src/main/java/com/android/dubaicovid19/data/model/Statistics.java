package com.android.dubaicovid19.data.model;

public class Statistics {
    public String Active;
    public String Confirmed;
    public String Deceased;
    public String Recovered;

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(String confirmed) {
        Confirmed = confirmed;
    }

    public String getDeceased() {
        return Deceased;
    }

    public void setDeceased(String deceased) {
        Deceased = deceased;
    }

    public String getRecovered() {
        return Recovered;
    }

    public void setRecovered(String recovered) {
        Recovered = recovered;
    }
}
