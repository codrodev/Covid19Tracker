package com.android.dubaicovid19.view.viewModels;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.PostRegisterUserModel;
import com.android.dubaicovid19.data.model.RegisterUserResponse;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.activities.OTPActivity;
import com.android.dubaicovid19.view.activities.RegistrationActivity;
import com.android.dubaicovid19.view.activities.SplashActivity;
import com.android.dubaicovid19.view.adapters.RegistrationPagerAdapter;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;

import java.lang.reflect.Method;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.content.Context.BLUETOOTH_SERVICE;
import static com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController.MY_PERMISSIONS_REQUEST_Location;
import static com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController.REQUEST_ENABLE_BT;

public class RegistrationParentFragmentViewModel extends ViewModel {

    RegistrationPagerAdapter pagerAdapter;
    private CovidTrackerApp covidTrackerApp;
    RegisterRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static PostRegisterUserModel postRegisterUserModel;
    SharedPreferences pref;
    BluetoothAdapter adapter;

    public RegistrationParentFragmentViewModel(Activity activity, RegisterRepository repository){
        this.activity = activity;
        pref = activity.getSharedPreferences("MyPref", 0);
        this.repository = repository;
        covidTrackerApp = CovidTrackerApp.create(activity);
        postRegisterUserModel = new PostRegisterUserModel();
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public RegistrationPagerAdapter getRegistrationPagerAdapter() {
        return pagerAdapter;
    }

    public void setRegistrationPagerAdapter(Fragment fr) {
        pagerAdapter = new RegistrationPagerAdapter(fr.getChildFragmentManager());
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

    public void enableAllPermission() {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(activity, AppConstants.ALL_PERMISSIONS, MY_PERMISSIONS_REQUEST_Location);
        } else
            enableBluetooth();


    }

    public void enableBluetooth() {
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public boolean isBluetoothEnabled(){
        return adapter.isEnabled();
    }

    public boolean isValidData(){
        boolean isValid = true;
        if(postRegisterUserModel == null){
            AlertDialogUtil.alertDialog("please fill data", activity);
            return isValid = false;
        }
        if(postRegisterUserModel.getUsername() == null || TextUtils.isEmpty(postRegisterUserModel.getUsername())){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_username), activity);
            return isValid = false;
        }

        if(!isValidMobile(String.valueOf(postRegisterUserModel.getMobile()))){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_mobile), activity);
            return isValid = false;
        }
        if(!isValidEmailId(postRegisterUserModel.getEmail())){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_email), activity);
            return isValid = false;
        }
        if(TextUtils.isEmpty(postRegisterUserModel.getNationalityCode())){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_nationality), activity);
            return isValid = false;
        }
        if(postRegisterUserModel.getAge() < 1){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_age), activity);
            return isValid = false;
        }
        if(TextUtils.isEmpty(postRegisterUserModel.getLat()) || Double.parseDouble(postRegisterUserModel.getLat()) == -1){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_location), activity);
            return isValid = false;
        }
        if(TextUtils.isEmpty(postRegisterUserModel.getLongitude()) || Double.parseDouble(postRegisterUserModel.getLongitude()) == -1){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_location), activity);
            return isValid = false;
        }
        if(TextUtils.isEmpty(postRegisterUserModel.geteIdOrPassport())){
            if(postRegisterUserModel.isEId()){
                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_emirate), activity);
            } else {
                AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_passport), activity);
            }

            return isValid = false;
        }
        if(postRegisterUserModel.getCovidInfoCode() == null || TextUtils.isEmpty(postRegisterUserModel.getCovidInfoCode())){
            AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.validate_covid), activity);
            return isValid = false;
        }
        return isValid;
    }

    public void apiRegisterUser(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);
        progressBar.setMessage(activity.getResources().getString(R.string.progress));
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();

        //postRegisterUserModel.setBLE_Address(getBlutoothAddress(activity));
        //mapNavigator.onStarted();
        String url = AppConstants.REGISTER_USER;
        postRegisterUserModel.setUniqueID(getUUID());
        Disposable disposable = repository.postRegisterUserAPI(url, postRegisterUserModel)
                .subscribeOn(covidTrackerApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterUserResponse>() {
                    @Override public void accept(RegisterUserResponse response) throws Exception {
                        if(response != null){
                            progressBar.dismiss();
                            if(response.Status.toLowerCase().equals("success")){
                                int userId = response.UserId;
                                storeInfo(postRegisterUserModel, response.UserId);
                            } else {
                                //storeInfo(postRegisterUserModel, 111);
                                AlertDialogUtil.alertDialog(response.Msg, activity);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        progressBar.dismiss();
                        storeInfo(postRegisterUserModel, -1);
                        //showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

   /* public String getBluetoothAddress(){
        *//*BluetoothManager manager = (BluetoothManager) activity.getSystemService(BLUETOOTH_SERVICE);
        String str = manager.getAdapter().getAddress();*//*
        return android.provider.Settings.Secure.getString(activity.getContentResolver(), "bluetooth_address");
    }*/

    private String getBlutoothAddress(Context mContext){
        // Check version API Android
        BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        String macAddress;

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {
            macAddress = Settings.Secure.getString(mContext.getContentResolver(), "bluetooth_address");
        } else {
            // Do this for phones running an SDK before lollipop
            macAddress = myBluetoothAdapter.getAddress();
        }
        return macAddress;
    }

    public String getUUID(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        try {
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);

            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);

            return uuids[0].getUuid().toString();
        } catch (Exception ex){

        }
        return "";
    }

    private void storeInfo(PostRegisterUserModel postRegisterUserModel, int userID){
        SharedPreferences.Editor edit = pref.edit();

        /*edit.putInt(AppConstants.USER_ID_KEY, userID);
        edit.putString(AppConstants.USER_NAME_KEY, postRegisterUserModel.getUsername());
        edit.putString(AppConstants.ID_PROOF_KEY, postRegisterUserModel.geteIdOrPassport());
        edit.putString(AppConstants.LAT_KEY, postRegisterUserModel.getLat());
        edit.putString(AppConstants.LONG_KEY, postRegisterUserModel.getLongitude());
        edit.putString(AppConstants.UUID_KEY, postRegisterUserModel.getUniqueID());
        edit.putString(AppConstants.COVID_KEY, postRegisterUserModel.getCovidInfoCode());*/

        edit.putInt(AppConstants.USER_ID_KEY, 110);
        edit.putString(AppConstants.USER_NAME_KEY, "Saty");
        edit.putString(AppConstants.ID_PROOF_KEY, "123");
        edit.putString(AppConstants.LAT_KEY, "17.410441");
        edit.putString(AppConstants.LONG_KEY, "78.544299");
        edit.putString(AppConstants.UUID_KEY, "da");
        edit.putString(AppConstants.COVID_KEY, "safe");

        edit.commit();

        Intent intent = OTPActivity.newIntent(activity);
        activity.startActivity(intent);
        activity.finish();
    }


}
