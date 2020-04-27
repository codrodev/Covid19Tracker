package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.PostNearByContainmentZone;
import com.android.dubaicovid19.data.model.ResponseContainmentCluster;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class ContainmentClusterRepository {
    private MyApiService api;

    public ContainmentClusterRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseContainmentCluster> getContainmentCluster(String url){
        return api.getContainmentCluster(url);
    }

    public Observable<ResponseContainmentCluster> getNearByContainmentCluster(String url, PostNearByContainmentZone model){
        return api.getNearByContainmentCluster(url, model);
    }
}
