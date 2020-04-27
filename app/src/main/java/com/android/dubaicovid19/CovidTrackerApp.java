package com.android.dubaicovid19;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.MyApiService;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class CovidTrackerApp extends Application {

    private MyApiService apiService;
    private NetworkConnectionInterceptor networkConnectionInterceptor;
    private Scheduler scheduler;



    private static CovidTrackerApp covidTrackerApp = null;

    public static synchronized CovidTrackerApp getInstance() {
        return covidTrackerApp;
    }

    private static CovidTrackerApp get(Context context) {
        return (CovidTrackerApp) context.getApplicationContext();
    }

    public static CovidTrackerApp create(Context context) {
        return CovidTrackerApp.get(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        covidTrackerApp = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        networkConnectionInterceptor = new NetworkConnectionInterceptor(covidTrackerApp);
    }
    public MyApiService getApiService() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(apiService == null){
            apiService = ApiFactory.getClient(networkConnectionInterceptor);
        }

        return apiService;
    }
    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }
}
