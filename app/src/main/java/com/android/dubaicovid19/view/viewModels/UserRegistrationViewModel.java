package com.android.dubaicovid19.view.viewModels;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.view.fragments.RegistrationParentFragment;

public class UserRegistrationViewModel extends ViewModel {

    public UserRegistrationViewModel(){

    }

    public  boolean isValidEmailId(String emailNumber) {
        boolean isValid = true;
        if (TextUtils.isEmpty(emailNumber)) {
            isValid = false;
            return isValid;
        } else if (!emailNumber.contains("@") || !emailNumber.contains(".")) {
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    public boolean isValidMobile(String mobileNumber){
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

    public void setUserInformation(String userName, boolean isEID, String idEmirate, String passport, String mobile, String emailID, double lat, double longitude,
                                   String gender, String nationality, String age){
        RegistrationParentFragmentViewModel.postRegisterUserModel.setEId(isEID);
        if(!TextUtils.isEmpty(userName)){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setUsername(userName);
        }
        if(isEID){
            if(!TextUtils.isEmpty(idEmirate)){

                RegistrationParentFragmentViewModel.postRegisterUserModel.seteIdOrPassport(idEmirate);
            }
        } else {
            if(!TextUtils.isEmpty(passport)){

                RegistrationParentFragmentViewModel.postRegisterUserModel.seteIdOrPassport(passport);
            }
        }

        if(isValidMobile(mobile)){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setMobile(Long.parseLong(mobile));
        }
        if(isValidEmailId(emailID)){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setEmail(emailID);
        }
        if(!TextUtils.isEmpty(String.valueOf(lat))){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setLat(String.valueOf(lat));
        }
        if(!TextUtils.isEmpty(String.valueOf(longitude))){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setLongitude(String.valueOf(longitude));
        }
        if(!TextUtils.isEmpty(gender)){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setGenderCode(gender);
        }
        if(!TextUtils.isEmpty(nationality)){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setNationalityCode(nationality);
        } else {
            RegistrationParentFragmentViewModel.postRegisterUserModel.setNationalityCode("");
        }
        if(!TextUtils.isEmpty(String.valueOf(age))){
            RegistrationParentFragmentViewModel.postRegisterUserModel.setAge(Integer.parseInt(age));
        } else {
            RegistrationParentFragmentViewModel.postRegisterUserModel.setAge(0);
        }
    }
}
