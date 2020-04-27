package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.ResponseChartData;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetLiveUpdates;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class CovidTrackerRepository {
    private MyApiService api;

    public CovidTrackerRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseGetLiveUpdates> getLiveUpdates(String url){
        return api.getLiveUpdates(url);
    }

    public Observable<ResponseChartData> getChartData(String url){
        return api.getChartData(url);
    }
}
