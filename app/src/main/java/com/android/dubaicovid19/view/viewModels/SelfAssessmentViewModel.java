package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.PostSelfAssessment;
import com.android.dubaicovid19.data.model.ResponseSelfAssessmentResult;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.data.repositories.SelfAssessmentRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.navigators.FragmentNavigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SelfAssessmentViewModel extends ViewModel {

    private CovidTrackerApp covidTrackerApp;
    SelfAssessmentRepository repository;
    Activity activity;
    FragmentNavigator frNavigator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SelfAssessmentViewModel(Activity activity, SelfAssessmentRepository repository){
        this.activity = activity;
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
    }

    public void apiSelfAssessment(PostSelfAssessment selfAssessment){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        //mapNavigator.onStarted();
        String url = AppConstants.POST_SELF_ASSESSMENT;

        Disposable disposable = repository.selfAssessmentTest(url, selfAssessment)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseSelfAssessmentResult>() {
                    @Override public void accept(ResponseSelfAssessmentResult response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                //storeInfo(postRegisterUserModel, response.UserId);
                                ArrayList al = new ArrayList<>();
                                al.add(response.getReco());
                                al.add(response.getColor());
                                navigate(activity, FragmentTAGS.FR_ASSESSMENT_RESULT, al);
                            } else {
                                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.api_error), activity);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        progressBar.dismiss();
                        //showErrorMessage(throwable.getMessage());
                        AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.api_error), activity);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void navigate(Context ctx, String fragmentTag, List<Object> param){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.fragmentNavigator(fragmentTag, true, param);
    }


}
