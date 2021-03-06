package com.android.dubaicovid19.data.network;



import com.android.dubaicovid19.utility.constant.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    public static MyApiService getClient(NetworkConnectionInterceptor networkConnectionInterceptor) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()

                .addInterceptor(networkConnectionInterceptor).connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .sslSocketFactory(tlsSocketFactory, Objects.requireNonNull(tlsSocketFactory.getTrustManager()))
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApiService.class);

    }
}
