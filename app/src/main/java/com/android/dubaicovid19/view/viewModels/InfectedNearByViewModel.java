package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.NearByInfectedUser;
import com.android.dubaicovid19.data.model.PostGetNearByCovidCenter;
import com.android.dubaicovid19.data.model.ResponseNeighbourhoodInfectedUserCount;
import com.android.dubaicovid19.data.repositories.InfectedNearByRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.InfectedNearByNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class InfectedNearByViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    InfectedNearByRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public InfectedNearByNavigator navigator;
    public int infectedUserCount;
    public static List<NearByInfectedUser> lstNearByInfectedUser;

    public int confirm = 0, active = 0, recovered = 0, deceased = 0;

    public InfectedNearByViewModel(Activity activity, InfectedNearByRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiGetNearByInfectedUser(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String url = AppConstants.GET_NEAR_BY_INFECTED_USER;
        PostGetNearByCovidCenter model = new PostGetNearByCovidCenter();
        model.setLat(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lat","")));
        model.setLon(Double.parseDouble(activity.getSharedPreferences("MyPref", 0).getString("lon","")));
        model.setUserId(activity.getSharedPreferences("MyPref", 0).getInt(AppConstants.USER_ID_KEY,-1));

        Disposable disposable = repository.getNearByInfectedUser(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseNeighbourhoodInfectedUserCount>() {
                    @Override public void accept(ResponseNeighbourhoodInfectedUserCount response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                updateInfectedUser(response.getNearByInfectedUserCount(), response.getNearByInfectedUser());
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

    private void updateInfectedUser(int infectedUser, List<NearByInfectedUser> lstInfectedUser){
        lstNearByInfectedUser = new ArrayList<>();
        lstNearByInfectedUser = lstInfectedUser;
        setInfectedUserCount(infectedUser);
        navigator.onSuccess();
    }

    public int getInfectedUserCount() {
        return infectedUserCount;
    }

    public void setInfectedUserCount(int infectedUserCount) {
        this.infectedUserCount = infectedUserCount;
    }
}
