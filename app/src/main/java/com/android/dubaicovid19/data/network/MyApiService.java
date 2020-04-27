package com.android.dubaicovid19.data.network;

import com.android.dubaicovid19.data.model.GeneralResponse;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.PostNearByContainmentZone;
import com.android.dubaicovid19.data.model.PostNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.model.PostRegisterUserModel;
import com.android.dubaicovid19.data.model.PostSelfAssessment;
import com.android.dubaicovid19.data.model.PostUpdateCovidLog;
import com.android.dubaicovid19.data.model.PostUserMovementTrace;
import com.android.dubaicovid19.data.model.RegisterUserResponse;
import com.android.dubaicovid19.data.model.ResponseAnnouncements;
import com.android.dubaicovid19.data.model.ResponseChartData;
import com.android.dubaicovid19.data.model.ResponseContainmentCluster;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetLiveUpdates;
import com.android.dubaicovid19.data.model.ResponseGetTestStatistics;
import com.android.dubaicovid19.data.model.ResponseNearByCovidRegisteredDevice;
import com.android.dubaicovid19.data.model.ResponseNeighbourhoodInfectedUserCount;
import com.android.dubaicovid19.data.model.ResponseSelfAssessmentResult;
import com.android.dubaicovid19.data.model.ResponseUserMovementTrace;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MyApiService {


    //to get the profile documents if any
    @POST
    Observable<RegisterUserResponse> registerUserAPI(@Url String url, @Body PostRegisterUserModel model);

    @POST
    Observable<ResponseUserMovementTrace> userMovementTrace(@Url String url, @Body PostUserMovementTrace model);

    @POST
    Observable<ResponseNearByCovidRegisteredDevice> nearByRegisterdDevices(@Url String url, @Body PostNearByCovidRegisteredDevice model);

    @POST
    Observable<ResponseSelfAssessmentResult> selfAssessmentTest(@Url String url, @Body PostSelfAssessment model);

    @POST
    Observable<GeneralResponse> updateCovidLog(@Url String url, @Body PostUpdateCovidLog model);

    @GET
    Observable<ResponseGetCovidCenter> getCovidCenter(@Url String url);

    @GET
    Observable<ResponseContainmentCluster> getContainmentCluster(@Url String url);

    @POST
    Observable<ResponseContainmentCluster> getNearByContainmentCluster(@Url String url, @Body PostNearByContainmentZone model);

    @GET
    Observable<ResponseGetTestStatistics> getTestStatistics(@Url String url);

    @GET
    Observable<ResponseAnnouncements> getGovtAnnouncement(@Url String url);

    @POST
    Observable<ResponseGetCovidCenter> getNearByCovidCenter(@Url String url, @Body PostGetNearByCovidCenter model);

    @POST
    Observable<ResponseNeighbourhoodInfectedUserCount> getNearByInfectedUser(@Url String url, @Body PostGetNearByCovidCenter model);

    @GET
    Observable<ResponseGetLiveUpdates> getLiveUpdates(@Url String url);

    @GET
    Observable<ResponseChartData> getChartData(@Url String url);

    @POST
    Observable<ResponseNearByCovidRegisteredDevice> nearByDevicesByLoc(@Url String url, @Body PostGetNearByCovidCenter model);

}
