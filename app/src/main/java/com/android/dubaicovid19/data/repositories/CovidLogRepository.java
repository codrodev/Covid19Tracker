package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.GeneralResponse;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.PostUpdateCovidLog;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class CovidLogRepository {
    private MyApiService api;

    public CovidLogRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<GeneralResponse> updateCovidLog(String url, PostUpdateCovidLog model){
        return api.updateCovidLog(url, model);
    }
}
