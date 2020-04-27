package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.Announcements;
import com.android.dubaicovid19.data.model.ResponseAnnouncements;
import com.android.dubaicovid19.data.repositories.GovtAnnouncementRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.adapters.GovtAnnouncementAdapter;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GovtAnnouncementViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    GovtAnnouncementRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<Announcements>> mutableAnnouncements = new MutableLiveData<>();
    GovtAnnouncementAdapter adapter;
    public CovidCenterNavigator navigator;

    public GovtAnnouncementViewModel(Activity activity, GovtAnnouncementRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiGetGovtAnnouncement(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String locale = AppConstants.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "EN" : "AR";
        String url = AppConstants.GET_GOVT_ANNOUNCEMENT + "?locale=" + locale;

        Disposable disposable = repository.getGovtAnnouncement(url)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseAnnouncements>() {
                    @Override public void accept(ResponseAnnouncements response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.getStatus().toLowerCase().equals("success")){
                                if(response.getAnnouncements() != null && response.getAnnouncements().size() > 0){
                                    setGovtAnnouncement(response.getAnnouncements());
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

    public void setGovtAnnouncement(List<Announcements> lstAnnouncements){
        mutableAnnouncements = new MutableLiveData<>();
        mutableAnnouncements.setValue(lstAnnouncements);

        adapter = new GovtAnnouncementAdapter(R.layout.adapter_govt_announcement, this, activity);
        setAnnouncementAdapter(lstAnnouncements);
        navigator.onSuccess();
    }

    public void  setAnnouncementAdapter(List<Announcements> lstAnnouncements) {
        this.adapter.setAnnouncements(lstAnnouncements);
        this.adapter.notifyDataSetChanged();
    }

    public GovtAnnouncementAdapter getAdapter() {
        return adapter;
    }

    public Announcements getCurrentAnnouncements(int position){
        if (mutableAnnouncements.getValue() != null ) {
            return mutableAnnouncements.getValue().get(position);
        }
        return null;
    }

    public MutableLiveData<List<Announcements>> getMutableAnnouncements(){
        return mutableAnnouncements;
    }
}
