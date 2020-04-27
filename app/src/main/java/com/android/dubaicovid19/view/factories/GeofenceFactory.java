package com.android.dubaicovid19.view.factories;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.GeofenceRepository;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.view.viewModels.MainViewModel;
import com.android.dubaicovid19.view.viewModels.RegistrationParentFragmentViewModel;

public class GeofenceFactory extends ViewModelProvider.NewInstanceFactory {
    private GeofenceRepository repository;
    private Context appContext;

    public GeofenceFactory(Context context, GeofenceRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    /*@NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        //return (T) new MainViewModel(appContext, repository);
    }*/
}