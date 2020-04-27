package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.model.ResponseNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class BluetoothRepository {
    private MyApiService api;

    public BluetoothRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseNearByCovidRegisteredDevice> nearByRegisterdDevices(String url, PostNearByCovidRegisteredDevice model){
        return api.nearByRegisterdDevices(url, model);
    }
}
