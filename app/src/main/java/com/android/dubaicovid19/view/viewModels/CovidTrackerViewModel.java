package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.ChartData;
import com.android.dubaicovid19.data.model.ResponseChartData;
import com.android.dubaicovid19.data.model.ResponseGetLiveUpdates;
import com.android.dubaicovid19.data.model.Statistics;
import com.android.dubaicovid19.data.repositories.CovidTrackerRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.navigators.CovidTrackerNavigator;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CovidTrackerViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    CovidTrackerRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CovidTrackerNavigator navigator;

    public String confirm = "", active = "", recovered = "", deceased = "";

    public CovidTrackerViewModel(Activity activity, CovidTrackerRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiChartData(final String type){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        String url = AppConstants.GET_CHART_DATA + type;

        Disposable disposable = repository.getChartData(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseChartData>() {
                    @Override public void accept(ResponseChartData response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                setChartTracker(response.ChartData, type);
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

    public void setChartTracker(List<ChartData> lstChart, String type){
        navigator.onChartSuccess(lstChart, type);
    }

    public void apiGetCovidLiveData(String type){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        String url = AppConstants.GET_LIVE_DATA + type;

        Disposable disposable = repository.getLiveUpdates(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseGetLiveUpdates>() {
                    @Override public void accept(ResponseGetLiveUpdates response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                setCovidTracker(response.getStatistics());
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



    public void setCovidTracker(Statistics stats){
            setConfirm(stats.getConfirmed());
            setActive(stats.getActive());
            setRecovered(stats.getRecovered());
            setDeceased(stats.getDeceased());
            navigator.onSuccess();
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String  confirm) {
        this.confirm = confirm;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }
}
