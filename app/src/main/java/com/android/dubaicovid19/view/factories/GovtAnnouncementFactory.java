package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.GovtAnnouncementRepository;
import com.android.dubaicovid19.view.viewModels.GovtAnnouncementViewModel;

public class GovtAnnouncementFactory extends ViewModelProvider.NewInstanceFactory {
    private GovtAnnouncementRepository repository;
    private Activity appContext;

    public GovtAnnouncementFactory(Activity context, GovtAnnouncementRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new GovtAnnouncementViewModel(appContext, repository);
    }
}