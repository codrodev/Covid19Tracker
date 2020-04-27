package com.android.dubaicovid19.view.navigators;

import com.android.dubaicovid19.data.model.ChartData;

import java.util.List;

public interface CovidTrackerNavigator {
    public void onSuccess();

    public void onChartSuccess(List<ChartData> lstChart, String type);
}
