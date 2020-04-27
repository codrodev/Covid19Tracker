package com.android.dubaicovid19.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;

import com.android.dubaicovid19.utility.constant.AppConstants;

import java.util.Locale;

public class Global {

    public static boolean isEdit = false;
    public static boolean isConnected(Context context) {

        NetworkInfo activeNetworkInfo=null;

        if(context!=null){
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();}
        if (activeNetworkInfo == null) return false;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static boolean isOreoSupported(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeLang(String lang, Context context) {
        Locale locale;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.USER_LANGUAGE, lang).apply();
        locale = new Locale(lang);
        AppConstants.CURRENT_LOCALE = lang;
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.N){
            config.setLocale(locale);
            context.createConfigurationContext(config);
        }
        else{
            config.locale = locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }
    }


    public static int riskDevice = -1;
    public static int safeDevice = -1;
    public static boolean isBluetoothScan = false;
}
