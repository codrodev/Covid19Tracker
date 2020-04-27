package com.android.dubaicovid19.view.factories;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.BluetoothRepository;
import com.android.dubaicovid19.data.repositories.GeofenceRepository;

public class BluetoothFactory extends ViewModelProvider.NewInstanceFactory {

    private BluetoothRepository repository;
    private Context appContext;

    public BluetoothFactory(Context context, BluetoothRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }
}