package com.android.dubaicovid19.view.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.dubaicovid19.data.repositories.SelfAssessmentRepository;
import com.android.dubaicovid19.view.viewModels.SelfAssessmentViewModel;

public class SelfAssessmentFactory extends ViewModelProvider.NewInstanceFactory {
    private SelfAssessmentRepository repository;
    private Activity appContext;

    public SelfAssessmentFactory(Activity context, SelfAssessmentRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new SelfAssessmentViewModel(appContext, repository);
    }
}