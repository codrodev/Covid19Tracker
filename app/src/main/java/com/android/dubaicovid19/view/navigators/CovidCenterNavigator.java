package com.android.dubaicovid19.view.navigators;

public interface CovidCenterNavigator {

    public void onSuccess();
    public void onEmpty(String Msg);
    public void onFailure(String Msg);
}
