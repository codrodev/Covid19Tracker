package com.android.dubaicovid19.data.model;

public class ResponseSelfAssessmentResult {
    public String Reco;
    public int Code;
    public String Status;
    public String Color;

    public String getReco() {
        return Reco;
    }

    public void setReco(String reco) {
        Reco = reco;
    }

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

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
