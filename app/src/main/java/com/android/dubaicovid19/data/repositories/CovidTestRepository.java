package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetTestStatistics;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class CovidTestRepository {
    private MyApiService api;

    public CovidTestRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseGetTestStatistics> getTestStatistics(String url){
        return api.getTestStatistics(url);
    }
}
