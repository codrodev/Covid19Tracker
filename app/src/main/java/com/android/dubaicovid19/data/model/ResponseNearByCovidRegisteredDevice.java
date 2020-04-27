package com.android.dubaicovid19.data.model;

public class ResponseNearByCovidRegisteredDevice {
    public int Code;
    public String Status;
    public int riskDeviceCount;
    public int safeDeviceCount;

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

    public int getRiskDevice() {
        return riskDeviceCount;
    }

    public void setRiskDevice(int riskDeviceCount) {
        this.riskDeviceCount = riskDeviceCount;
    }

    public int getSafeDevice() {
        return safeDeviceCount;
    }

    public void setSafeDevice(int safeDeviceCount) {
        this.safeDeviceCount = safeDeviceCount;
    }
}
