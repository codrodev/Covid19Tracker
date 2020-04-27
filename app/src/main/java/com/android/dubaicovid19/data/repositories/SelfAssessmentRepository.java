package com.android.dubaicovid19.data.repositories;

import com.android.dubaicovid19.data.model.PostSelfAssessment;
import com.android.dubaicovid19.data.model.ResponseSelfAssessmentResult;
import com.android.dubaicovid19.data.network.MyApiService;

import io.reactivex.Observable;

public class SelfAssessmentRepository {
    private MyApiService api;

    public SelfAssessmentRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseSelfAssessmentResult> selfAssessmentTest(String url, PostSelfAssessment model){
        return api.selfAssessmentTest(url, model);
    }
}
