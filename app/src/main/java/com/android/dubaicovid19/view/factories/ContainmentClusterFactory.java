package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.ContainmentClusterRepository;
import com.android.dubaicovid19.view.viewModels.ContainmentClusterViewModel;

public class ContainmentClusterFactory extends ViewModelProvider.NewInstanceFactory {
    private ContainmentClusterRepository repository;
    private Activity appContext;

    public ContainmentClusterFactory(Activity context, ContainmentClusterRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new ContainmentClusterViewModel(appContext, repository);
    }
}