package com.android.dubaicovid19.view.viewModels;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

public class CovidInfoViewModel extends ViewModel {

    public CovidInfoViewModel(){

    }

    public void setConvidInfo(String covidInfo){
        if(!TextUtils.isEmpty(covidInfo)) {
            RegistrationParentFragmentViewModel.postRegisterUserModel.setCovidInfoCode(covidInfo);
        }
    }
}
