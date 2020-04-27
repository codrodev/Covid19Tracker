package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.GeneralResponse;
import com.android.dubaicovid19.data.model.PostUpdateCovidLog;
import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.CovidLogRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CovidLogViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    CovidLogRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CovidLogViewModel(Activity activity, CovidLogRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiUpdateCovidLog(String name, String id, boolean isEID, double lat, double lon, String result, String mobileNo){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String url = AppConstants.UPDATE_COVID_LOG;
        PostUpdateCovidLog model = new PostUpdateCovidLog();
        model.setName(name);
        model.setEid(isEID);
        model.setEIdOrPassport(id);
        model.setLat(lat);
        model.setLon(lon);
        model.setTestResult(result);
        model.setMobile(mobileNo);

        Disposable disposable = repository.updateCovidLog(url, model)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GeneralResponse>() {
                    @Override public void accept(GeneralResponse response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                AlertDialogUtil.alertDialog(response.Msg, activity);
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

    private boolean isValidMobile(String mobileNumber){
        boolean isValid = true;
        if (TextUtils.isEmpty(mobileNumber)) {
            isValid = false;
            return isValid;
        }
        if (mobileNumber.length() < 7) {
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    public boolean isValidData(String name, String id, boolean isEID, double lat, double lon, String result, String mobileNo){
        boolean isValid = true;
        if(name == null || TextUtils.isEmpty(name)){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_username), activity);
            return isValid = false;
        }

        if(!isValidMobile(String.valueOf(mobileNo))){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_mobile), activity);
            return isValid = false;
        }

        if(lat < 0 || lon < 0){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_location), activity);
            return isValid = false;
        }

        if(TextUtils.isEmpty(id)){
            if(isEID){
                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_emirate), activity);
            } else {
                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_passport), activity);
            }

            return isValid = false;
        }
        return isValid;
    }
}
