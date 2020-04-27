package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.ResponseGetCovidCenter;
import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.adapters.CovidCenterAdapter;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CovidCenterViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    CovidCenterRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<CovidCenters>> mutableCovidCenter = new MutableLiveData<>();
    CovidCenterAdapter adapterCovidCenter;
    public CovidCenterNavigator navigator;

    public CovidCenterViewModel(Activity activity, CovidCenterRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiGetNearByCovidCenter(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String locale = AppConstants.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "EN" : "AR";
        String url = AppConstants.GET_NEAR_BY_COVID_CENTER + "?locale=" + locale;
        PostGetNearByCovidCenter model = new PostGetNearByCovidCenter();
        model.setLat(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lat","")));
        model.setLon(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lon","")));

        Disposable disposable = repository.getNearByCovidCenter(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseGetCovidCenter>() {
                    @Override public void accept(ResponseGetCovidCenter response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                if(response.CovidCenters != null && response.CovidCenters.length > 0){
                                    setCovidCenter(Arrays.asList(response.CovidCenters));
                                } else {
                                    navigator.onEmpty(activity.getResources().getString(R.string.no_available_center));
                                    setCovidCenter(new ArrayList<CovidCenters>());
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

    public void apiGetCovidCenter(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String locale = AppConstants.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "EN" : "AR";
        String url = AppConstants.GET_COVID_CENTER + "?locale=" + locale;

        Disposable disposable = repository.getCovidCenter(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseGetCovidCenter>() {
                    @Override public void accept(ResponseGetCovidCenter response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                if(response.CovidCenters != null && response.CovidCenters.length > 0){
                                    setCovidCenter(Arrays.asList(response.CovidCenters));
                                } else {
                                    navigator.onEmpty(activity.getResources().getString(R.string.no_available_center));
                                    setCovidCenter(new ArrayList<CovidCenters>());
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

    public void setCovidCenter(List<CovidCenters> lstCovidCenter){
        mutableCovidCenter = new MutableLiveData<>();
        mutableCovidCenter.setValue(lstCovidCenter);

        adapterCovidCenter = new CovidCenterAdapter(R.layout.adapter_covid_center, this, activity);
        setCovidCenterAdapter(lstCovidCenter);
        navigator.onSuccess();
    }

    public void  setCovidCenterAdapter(List<CovidCenters> lstCovidCenter) {
        this.adapterCovidCenter.setCovidCenterInfo(lstCovidCenter);
        this.adapterCovidCenter.notifyDataSetChanged();
    }

    public CovidCenterAdapter getAdapter() {
        return adapterCovidCenter;
    }

    public CovidCenters getCurrentCovidCenter(int position){
        if (mutableCovidCenter.getValue() != null ) {
            return mutableCovidCenter.getValue().get(position);
        }
        return null;
    }

    public MutableLiveData<List<CovidCenters>> getMutableCovidCenter(){
        return mutableCovidCenter;
    }
}
