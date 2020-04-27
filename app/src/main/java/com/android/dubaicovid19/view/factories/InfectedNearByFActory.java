package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.InfectedNearByRepository;
import com.android.dubaicovid19.view.viewModels.InfectedNearByViewModel;

public class InfectedNearByFActory extends ViewModelProvider.NewInstanceFactory {
    private InfectedNearByRepository repository;
    private Activity appContext;

    public InfectedNearByFActory(Activity context, InfectedNearByRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new InfectedNearByViewModel(appContext, repository);
    }
}