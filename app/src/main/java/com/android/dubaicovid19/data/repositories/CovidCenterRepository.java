package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class CovidCenterRepository {
    private MyApiService api;

    public CovidCenterRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseGetCovidCenter> getCovidCenter(String url){
        return api.getCovidCenter(url);
    }

    public Observable<ResponseGetCovidCenter> getNearByCovidCenter(String url, PostGetNearByCovidCenter model){
        return api.getNearByCovidCenter(url, model);
    }
}
