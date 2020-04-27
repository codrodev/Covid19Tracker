package com.android.dubaicovid19.data.model;

import com.google.gson.annotations.SerializedName;

public class PostSelfAssessment {
    @SerializedName("IsRecentlyTravelled")
    private boolean IsRecentlyTravelled;

    @SerializedName("IsCameIntoCloseContact")
    private boolean IsCameIntoCloseContact;

    @SerializedName("IsHavingFever")
    private boolean IsHavingFever;

    @SerializedName("IsFirstResponder")
    private boolean IsFirstResponder;

    public boolean isRecentlyTravelled() {
        return IsRecentlyTravelled;
    }

    public void setRecentlyTravelled(boolean recentlyTravelled) {
        IsRecentlyTravelled = recentlyTravelled;
    }

    public boolean isCameIntoCloseContact() {
        return IsCameIntoCloseContact;
    }

    public void setCameIntoCloseContact(boolean cameIntoCloseContact) {
        IsCameIntoCloseContact = cameIntoCloseContact;
    }

    public boolean isHavingFever() {
        return IsHavingFever;
    }

    public void setHavingFever(boolean havingFever) {
        IsHavingFever = havingFever;
    }

    public boolean isFirstResponder() {
        return IsFirstResponder;
    }

    public void setFirstResponder(boolean firstResponder) {
        IsFirstResponder = firstResponder;
    }
}
