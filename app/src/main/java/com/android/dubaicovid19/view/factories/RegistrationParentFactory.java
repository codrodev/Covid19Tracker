package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.view.viewModels.RegistrationParentFragmentViewModel;

public class RegistrationParentFactory extends ViewModelProvider.NewInstanceFactory {
    private RegisterRepository repository;
    private Activity appContext;

    public RegistrationParentFactory(Activity context, RegisterRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new RegistrationParentFragmentViewModel(appContext, repository);
    }
}