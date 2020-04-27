package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.CovidStatistics;
import com.android.dubaicovid19.data.model.ResponseGetTestStatistics;
import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.CovidTestRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CovidTestViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    CovidTestRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CovidCenterNavigator navigator;
    CovidStatistics statistics;

    public CovidTestViewModel(Activity activity, CovidTestRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiGetCovidTestStatistics(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String url = AppConstants.GET_COVID_TEST_STATS;

        Disposable disposable = repository.getTestStatistics(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseGetTestStatistics>() {
                    @Override public void accept(ResponseGetTestStatistics response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                if(response.getStatistics() != null){
                                    setCovidTest(response.getStatistics());
                                }
                            } else {
                                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.api_error), activity);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        progressBar.dismiss();
                        //showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void setCovidTest(List<CovidStatistics> stats){
        setStatistics(stats.get(2));
        navigator.onSuccess();
    }

    public CovidStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(CovidStatistics statistics) {
        this.statistics = statistics;
    }
}
