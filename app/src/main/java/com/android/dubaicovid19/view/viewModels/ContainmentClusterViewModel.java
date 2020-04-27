package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.ContainmentCluster;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.data.model.GeneralResponse;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.PostNearByContainmentZone;
import com.android.dubaicovid19.data.model.PostUpdateCovidLog;
import com.android.dubaicovid19.data.model.ResponseContainmentCluster;
import com.android.dubaicovid19.data.repositories.ContainmentClusterRepository;
import com.android.dubaicovid19.data.repositories.CovidLogRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.adapters.ClusterAdapter;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ContainmentClusterViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    ContainmentClusterRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<ContainmentCluster>> mutableContainmentCluster = new MutableLiveData<>();
    ClusterAdapter adapter;
    public CovidCenterNavigator navigator;
    public static List<ContainmentCluster> lstContainmentCluster;

    public static List<ContainmentCluster> getLstContainmentCluster() {
        return lstContainmentCluster;
    }

    public static void setLstContainmentCluster(List<ContainmentCluster> lstContainmentCluster) {
        ContainmentClusterViewModel.lstContainmentCluster = lstContainmentCluster;
    }

    public ContainmentClusterViewModel(Activity activity, ContainmentClusterRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiGetNearByContainmentCluster(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String locale = AppConstants.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "EN" : "AR";
        String url = AppConstants.NEAR_BY_CONTAINMENT_CLUSTER + "?locale=" + locale;

        PostNearByContainmentZone model = new PostNearByContainmentZone();
        model.setLat(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lat","")));
        model.setLon(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lon","")));

        Disposable disposable = repository.getNearByContainmentCluster(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseContainmentCluster>() {
                    @Override public void accept(ResponseContainmentCluster response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                if(response.getContainmentCluster() != null && response.getContainmentCluster().size() > 0){
                                    setCluster(response.getContainmentCluster());
                                } else {
                                    navigator.onEmpty(activity.getResources().getString(R.string.no_available_cluster));
                                    setCluster(new ArrayList<ContainmentCluster>());
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

    public void apiGetContainmentCluster(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String locale = AppConstants.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "EN" : "AR";
        String url = AppConstants.GET_CONTAINMENT_CLUSTER + "?locale=" + locale;


        Disposable disposable = repository.getContainmentCluster(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseContainmentCluster>() {
                    @Override public void accept(ResponseContainmentCluster response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                if(response.getContainmentCluster() != null && response.getContainmentCluster().size() > 0){
                                    setCluster(response.getContainmentCluster());
                                } else {
                                    navigator.onEmpty(activity.getResources().getString(R.string.no_available_cluster));
                                    setCluster(new ArrayList<ContainmentCluster>());
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

    public void setCluster(List<ContainmentCluster> lstCluster){
        mutableContainmentCluster = new MutableLiveData<>();
        mutableContainmentCluster.setValue(lstCluster);
        adapter = new ClusterAdapter(R.layout.adapter_cluster, this, activity);
        setClusterAdapter(lstCluster);
        setLstContainmentCluster(lstCluster);
        navigator.onSuccess();
    }

    public void  setClusterAdapter(List<ContainmentCluster> lstCluster) {
        this.adapter.setContainmentCluster(lstCluster);
        this.adapter.notifyDataSetChanged();
    }

    public ClusterAdapter getAdapter() {
        return adapter;
    }

    public ContainmentCluster getCurrentCluster(int position){
        if (mutableContainmentCluster.getValue() != null ) {
            return mutableContainmentCluster.getValue().get(position);
        }
        return null;
    }

    public MutableLiveData<List<ContainmentCluster>> getMutableCluster(){
        return mutableContainmentCluster;
    }
}
