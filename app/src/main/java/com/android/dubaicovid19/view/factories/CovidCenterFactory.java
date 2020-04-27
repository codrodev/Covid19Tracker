package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;

public class CovidCenterFactory extends ViewModelProvider.NewInstanceFactory {
    private CovidCenterRepository repository;
    private Activity appContext;

    public CovidCenterFactory(Activity context, CovidCenterRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new CovidCenterViewModel(appContext, repository);
    }
}