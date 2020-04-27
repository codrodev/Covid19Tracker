package com.android.dubaicovid19.utility.services;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.android.dubaicovid19.utility.Global;

public class NotificationService {

    private Context mContext;
    private NotificationManager mNotifManager;
    private static NotificationService mNotificationService;
    private static final String SERVICE_CHANNEL = "ServiceChannel";
    public static final String DEFAULT_CHANNEL = "DefaultChannel";

    private NotificationService(Context context){
        this.mContext = context;
        if(null == mNotifManager) {
            mNotifManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }
        if(Global.isOreoSupported()) {
            createNotifChannels();
        }
    }

    public static NotificationService getInstance(Context context){
        if(mNotificationService == null){
            synchronized (NotificationService.class){
                if(mNotificationService == null){
                    mNotificationService = new NotificationService(context);
                }
            }
        }
        return mNotificationService;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotifChannels(){
        String default_title = "Default";
        NotificationChannel service_channel = new NotificationChannel(SERVICE_CHANNEL, default_title, NotificationManager.IMPORTANCE_DEFAULT);
        mNotifManager.createNotificationChannel(service_channel);

        String channel_title = "Standard";
        NotificationChannel default_channel = new NotificationChannel(DEFAULT_CHANNEL, channel_title, NotificationManager.IMPORTANCE_DEFAULT);
        mNotifManager.createNotificationChannel(default_channel);
    }

    public NotificationManager getNotificationManager() {
        return mNotifManager;
    }

    public String getServiceChannel() {
        return SERVICE_CHANNEL;
    }
}
