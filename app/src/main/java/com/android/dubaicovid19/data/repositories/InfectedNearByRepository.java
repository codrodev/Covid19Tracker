package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.ResponseNeighbourhoodInfectedUserCount;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class InfectedNearByRepository {
    private MyApiService api;

    public InfectedNearByRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<ResponseNeighbourhoodInfectedUserCount> getNearByInfectedUser(String url, PostGetNearByCovidCenter model){
        return api.getNearByInfectedUser(url, model);
    }
}
