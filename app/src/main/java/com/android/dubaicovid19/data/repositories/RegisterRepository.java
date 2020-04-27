package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostRegisterUserModel;
import com.android.dubaicovid19.data.model.RegisterUserResponse;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class RegisterRepository {

    private MyApiService api;

    public RegisterRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<RegisterUserResponse> postRegisterUserAPI(String url, PostRegisterUserModel model){
        return api.registerUserAPI(url, model);
    }
}
