package com.android.dubaicovid19.utility.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.PostUserMovementTrace;
import com.android.dubaicovid19.data.model.ResponseUserMovementTrace;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.GeofenceRepository;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.activities.SplashActivity;
import com.android.dubaicovid19.view.factories.GeofenceFactory;
import com.google.android.gms.location.LocationListener;

import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Service;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CovidLocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = CovidLocationService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();

    public static final String ACTION_LOCATION_BROADCAST = CovidLocationService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private NotificationService mNotifService = NotificationService.getInstance(CovidTrackerApp.getInstance());

    @Override
    public void onCreate() {
        super.onCreate();
        if(Global.isOreoSupported()){
            String title = getString(R.string.app_name);
            String content = getString(R.string.app_name) + " is running";
            Notification notification =
                    showForegroundServiceNotif(CovidTrackerApp.getInstance(), title, content, 100, 1);
            startForeground(1, notification);
        }
    }

    public Notification showForegroundServiceNotif(@NonNull Context context,
                                                          @NonNull String title,
                                                          @NonNull String content,
                                                          @NonNull int requestCode,
                                                          int notifId){
        Intent notifIntent = new Intent(context, SplashActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Note = need to create object here otherwise channel may not created and following code will get fail.

        Notification notification = new NotificationCompat.Builder(context, mNotifService.getServiceChannel())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_covid)
                .setContentIntent(contentIntent).build();
        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onTaskRemoved(intent);
        Log.d(TAG, "onStartCommand () == location");
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(10000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Connected to Google API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");


        if (location != null) {
            Log.d(TAG, "== location != null");
            Log.d(TAG, "Connection retained");
            if (calculateDistance(location) > 50) {
                //call api here
                int userID = getApplicationContext().getSharedPreferences("MyPref", 0).getInt("userID",-1);
                apiRegisterUser(location.getLatitude(), location.getLongitude(), userID);
            }
            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private float calculateDistance(Location loc) {
        Location locationA = new Location("point A");


        locationA.setLatitude(
                Double.parseDouble(getApplicationContext().getSharedPreferences("MyPref", 0).getString("lat","")));
        locationA.setLongitude(Double.parseDouble(getApplicationContext().getSharedPreferences("MyPref", 0).getString("lon","")));

        Location locationB = new Location("point B");

        locationB.setLatitude(loc.getLatitude());
        locationB.setLongitude(loc.getLongitude());

        float distance = locationA.distanceTo(locationB);

        return distance;
    }

    public void apiRegisterUser(double lat, double lon, int userID){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        String url = AppConstants.USER_MOVEMENT_TRACE;
        GeofenceRepository repository = null;
        GeofenceFactory factory;
        try {
            repository = new GeofenceRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new GeofenceFactory(getApplicationContext(), repository);
        CovidTrackerApp covidTrackerApp = CovidTrackerApp.create(getApplicationContext());
        PostUserMovementTrace model = new PostUserMovementTrace();
        model.setLat(String.valueOf(lat));
        model.setLongitude(String.valueOf(lon));
        model.setUserId(String.valueOf(userID));
        Disposable disposable = userMovementTrace(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseUserMovementTrace>() {
                    @Override public void accept(ResponseUserMovementTrace response) throws Exception {
                        if(response != null){
                            if(response.Status.toLowerCase().equals("success")){
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                                        .setContentTitle("COVID Tracker")
                                        .setContentText("User exits permitted region")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle())
                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setAutoCancel(true);

                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                notificationManager.notify(0, notificationBuilder.build());
                                //storeInfo(postRegisterUserModel, response.UserId);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {

                    }
                });

        compositeDisposable.add(disposable);
    }

    public Observable<ResponseUserMovementTrace> userMovementTrace(String url, PostUserMovementTrace model){
        try {
            return ApiFactory.getClient(new NetworkConnectionInterceptor(getApplicationContext())).userMovementTrace(url, model);
        } catch (Exception ex){

        }
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

}