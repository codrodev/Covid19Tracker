package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.CovidLogRepository;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;
import com.android.dubaicovid19.view.viewModels.CovidLogViewModel;

public class CovidLogFactory extends ViewModelProvider.NewInstanceFactory {
    private CovidLogRepository repository;
    private Activity appContext;

    public CovidLogFactory(Activity context, CovidLogRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new CovidLogViewModel(appContext, repository);
    }
}