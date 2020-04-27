package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.CovidTrackerRepository;
import com.android.dubaicovid19.view.viewModels.CovidTrackerViewModel;

public class CovidTrackerFactory extends ViewModelProvider.NewInstanceFactory {
    private CovidTrackerRepository repository;
    private Activity appContext;

    public CovidTrackerFactory(Activity context, CovidTrackerRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new CovidTrackerViewModel(appContext, repository);
    }
}