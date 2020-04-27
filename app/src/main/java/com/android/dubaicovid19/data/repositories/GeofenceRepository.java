package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostRegisterUserModel;
import com.android.dubaicovid19.data.model.PostUserMovementTrace;
import com.android.dubaicovid19.data.model.RegisterUserResponse;
import com.android.dubaicovid19.data.model.ResponseUserMovementTrace;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class GeofenceRepository {

    private MyApiService api;

    public GeofenceRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseUserMovementTrace> userMovementTrace(String url, PostUserMovementTrace model){
        return api.userMovementTrace(url, model);
    }
}
