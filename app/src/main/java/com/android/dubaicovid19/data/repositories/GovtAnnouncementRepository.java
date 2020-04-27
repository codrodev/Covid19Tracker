package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.ResponseAnnouncements;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class GovtAnnouncementRepository {
    private MyApiService api;

    public GovtAnnouncementRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<ResponseAnnouncements> getGovtAnnouncement(String url){
        return api.getGovtAnnouncement(url);
    }
}
