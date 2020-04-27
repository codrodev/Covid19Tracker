package com.android.dubaicovid19.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dubaicovid19.BuildConfig;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.GeofenceRepository;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.databinding.ActivityMainBinding;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.utility.services.CovidLocationService;
import com.android.dubaicovid19.utility.services.LocationService;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.factories.GeofenceFactory;
import com.android.dubaicovid19.view.factories.RegistrationParentFactory;
import com.android.dubaicovid19.view.fragments.ClusterMapFragment;
import com.android.dubaicovid19.view.fragments.ContainmentClusterFragment;
import com.android.dubaicovid19.view.fragments.CovidCenterFragment;
import com.android.dubaicovid19.view.fragments.CovidInfoFragment;
import com.android.dubaicovid19.view.fragments.CovidLogFragment;
import com.android.dubaicovid19.view.fragments.CovidTestFragment;
import com.android.dubaicovid19.view.fragments.CovidTrackerFragment;
import com.android.dubaicovid19.view.fragments.GovtAnnouncementFragment;
import com.android.dubaicovid19.view.fragments.HelplineFragment;
import com.android.dubaicovid19.view.fragments.HomeFragment;
import com.android.dubaicovid19.view.fragments.InfectedNearByFragment;
import com.android.dubaicovid19.view.fragments.MapFragment;
import com.android.dubaicovid19.view.fragments.PreventCovidFragment;
import com.android.dubaicovid19.view.fragments.RiskStatusFragment;
import com.android.dubaicovid19.view.fragments.SelfAssessmentFragment;
import com.android.dubaicovid19.view.fragments.SelfAssessmentResultFragment;
import com.android.dubaicovid19.view.fragments.TermsFragment;
import com.android.dubaicovid19.view.navigators.FragmentNavigator;
import com.android.dubaicovid19.view.viewModels.MainViewModel;
import com.android.dubaicovid19.view.viewModels.RegistrationParentFragmentViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentNavigator {

    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    MainViewModel model;
    ActivityMainBinding binding;
    GeofenceFactory factory;
    GeofenceRepository repository;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private boolean mAlreadyStartedService = false;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConstants.CURRENT_LOCALE =  "en";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        pref.getString(AppConstants.LAT_KEY, "");
        pref.getString(AppConstants.LONG_KEY, "");

        factory = new GeofenceFactory(this, repository);
        model = ViewModelProviders.of(this, factory).get(MainViewModel.class);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(CovidLocationService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(CovidLocationService.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null) {
                            //mMsgView.setText("Location:" + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                        }
                    }

                }, new IntentFilter(CovidLocationService.ACTION_LOCATION_BROADCAST)

        );
        openHomePage();
        /*imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.isEdit = true;
                loadFragment(FragmentTAGS.FR_COVID, true, null);
            }
        });*/
    }


    @Override
    public void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params) {
        loadFragment(fragmentTag, addToBackStack, params);
    }

    private void openHomePage(){
        loadFragment(FragmentTAGS.FR_HOME, false, null);
    }

    public Fragment loadFragment(String fragment_tag, Boolean addToBackStack, List<Object> params) {
        fragmentManager = getSupportFragmentManager();
        tx = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (fragment_tag) {
            case FragmentTAGS.FR_HOME:
                fragment = HomeFragment.newInstance();
                break;
            case FragmentTAGS.FR_RISK_STATUS:
                fragment = RiskStatusFragment.newInstance();
                break;
            case FragmentTAGS.FR_COVID_CENTER:
                fragment = CovidCenterFragment.newInstance();
                break;
            case FragmentTAGS.FR_PREVENT_COVID:
                fragment = PreventCovidFragment.newInstance();
                break;
            case FragmentTAGS.FR_SELF_ASSESSMENT:
                fragment = SelfAssessmentFragment.newInstance();
                break;
            case FragmentTAGS.FR_TERMS:
                fragment = TermsFragment.newInstance();
                break;
            case FragmentTAGS.FR_ASSESSMENT_RESULT:
                if(params!=null) {
                    fragment = SelfAssessmentResultFragment.newInstance(params.get(0).toString(), params.get(1).toString());
                }
                break;
            case FragmentTAGS.FR_COVID_TRACKER:
                fragment = CovidTrackerFragment.newInstance();
                break;
            case FragmentTAGS.FR_INFECTED_NEARBY:
                fragment = InfectedNearByFragment.newInstance();
                break;
            case FragmentTAGS.FR_MAP:
                fragment = MapFragment.newInstance();
                break;
            case FragmentTAGS.FR_GOVT_ANNOUNCEMENT:
                fragment = GovtAnnouncementFragment.newInstance();
                break;
            case FragmentTAGS.FR_HELP_LINE:
                fragment = HelplineFragment.newInstance();
                break;
            case FragmentTAGS.FR_COVID_TEST:
                fragment = CovidTestFragment.newInstance();
                break;
            case FragmentTAGS.FR_COVID_LOG:
                fragment = CovidLogFragment.newInstance();
                break;
            case FragmentTAGS.FR_COVID_CLUSTER:
                fragment = ContainmentClusterFragment.newInstance();
                break;
            case FragmentTAGS.FR_CLUSTER_MAP:
                fragment = ClusterMapFragment.newInstance();
                break;
            default:
                fragment = HomeFragment.newInstance();
                break;
        }

        tx.replace(R.id.ui_container, fragment, fragment_tag);

        if (addToBackStack)
            tx.addToBackStack(fragment_tag);
        tx.commitAllowingStateLoss();
        return fragment;
    }

    @Override

    public void onResume() {
        super.onResume();
        if(!getSharedPreferences("MyPref", 0).getString(AppConstants.COVID_KEY,"").toLowerCase().equals("safe")) {
            startStep1();
        }
    }


    private void startStep1() {
        if (isGooglePlayServicesAvailable()) {
            startStep2(null);
        } else {
            Toast.makeText(getApplicationContext(), "no_google_playservice_available", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (checkPermissions()) {
            startStep3();
        } else {
            requestPermissions();
        }
        return true;
    }


    private void promptInternetConnect() {
        AlertDialogUtil.alertDialog("no internet", this);
    }

    private void startStep3() {
        if (!mAlreadyStartedService) {
            if (Global.isOreoSupported()) {
                startForegroundService(new Intent(this, CovidLocationService.class));
            } else {
                startService(new Intent(this, CovidLocationService.class));
            }
            mAlreadyStartedService = true;
        }
    }


    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }

            return false;

        }

        return true;

    }

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationale2 = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale || shouldProvideRationale2) {
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }

                    });
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);

        }

    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStep3();
            } else {
                showSnackbar(R.string.permission_rationale,
                        R.string.settings, new View.OnClickListener() {

                            @Override

                            public void onClick(View view) {

                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        });
            }
        }

    }
}
