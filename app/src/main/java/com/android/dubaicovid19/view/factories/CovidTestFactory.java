package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.CovidTestRepository;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;
import com.android.dubaicovid19.view.viewModels.CovidTestViewModel;

public class CovidTestFactory extends ViewModelProvider.NewInstanceFactory {
    private CovidTestRepository repository;
    private Activity appContext;

    public CovidTestFactory(Activity context, CovidTestRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new CovidTestViewModel(appContext, repository);
    }
}