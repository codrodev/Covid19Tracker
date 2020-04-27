package com.android.dubaicovid19.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.databinding.FragmentUserRegistrationBinding;
import com.android.dubaicovid19.utility.InputFilterMinMax;
import com.android.dubaicovid19.utility.MapUtility;
import com.android.dubaicovid19.view.activities.LocationPickerActivity;
import com.android.dubaicovid19.view.custom.OnSpinerItemClick;
import com.android.dubaicovid19.view.custom.SpinnerDialog;
import com.android.dubaicovid19.view.viewModels.UserRegistrationViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController.MY_PERMISSIONS_REQUEST_Location;
import static com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController.REQUEST_ENABLE_BT;

public class UserRegistrationFragment extends Fragment implements RegistrationParentFragment.onNextListner {

    UserRegistrationViewModel model;
    FragmentUserRegistrationBinding binding;
    private View mRootView;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    SpinnerDialog spinnerDialog;
    boolean isEID;
    String gender;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    private boolean mLocationPermissionGranted;
    private Geocoder mGeocoder;
    double lat = -1, longitude = -1;
    int selectedAreaID = -1;

    public static UserRegistrationFragment newInstance() {
        Bundle args = new Bundle();
        UserRegistrationFragment fragment = new UserRegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(UserRegistrationViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_registration, container, false);
        binding.setVmFragmentUserRegistration(model);
        mRootView = binding.getRoot();
        initializePage();
        RegistrationParentFragment.listner = this;
        return binding.getRoot();
    }

    public boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int bluetoothAdminPermision = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_ADMIN);
        int bluetoothPermision = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (bluetoothAdminPermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (bluetoothPermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        //getSettingsLocation();
        return true;

    }

    private void initializePage(){
        checkAndRequestPermissions();
        populateCountry();
        binding.editAge.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "120")});
        isEID = true;
        gender = "M";

        binding.rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender = "M";
                }
            }
        });
        binding.rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender = "F";
                }
            }
        });
        binding.rbOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    gender = "O";
                }
            }
        });

        binding.rbEID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.editPassport.setVisibility(View.GONE);
                    binding.editEID.setVisibility(View.VISIBLE);
                    isEID = true;
                }
            }
        });

        binding.rbPassport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.editPassport.setVisibility(View.VISIBLE);
                    binding.editEID.setVisibility(View.GONE);
                    isEID = false;
                }
            }
        });

        /*binding.editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
            }
        });*/

        binding.edtNationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerDialog != null){
                    spinnerDialog.showSpinerDialog();
                }
            }
        });

        binding.layoutChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }

        }
    }

    /*private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };*/

    private void populateCountry() {
        List<String> area = new ArrayList<String>();
        List<String> areaID = new ArrayList<String>();
        area = Arrays.asList(getResources().getStringArray(R.array.country_list));
        areaID = Arrays.asList(getResources().getStringArray(R.array.country_list_code));
        spinnerDialog = new SpinnerDialog(getActivity(), area,
                getResources().getString(R.string.tap_to_choose));
        binding.edtNationality.setHint(getResources().getString(R.string.tap_to_choose));
        spinnerDialog.setTitleColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchIconColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchTextColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemDividerColor(getResources().getColor(R.color.black));
        spinnerDialog.setCloseColor(getResources().getColor(R.color.black));

        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //Toast.makeText(MainActivity.this, item + "  " + position + "", Toast.LENGTH_SHORT).show();
                //spinArea.setText(item + " Position: " + position);
                selectedAreaID = position;
                if(!TextUtils.isEmpty(item)) {
                    binding.edtNationality.setText(item);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    lat = data.getDoubleExtra(MapUtility.LATITUDE, -1);
                    longitude = data.getDoubleExtra(MapUtility.LONGITUDE, -1);
                    binding.editLocation.setText("Address: "+address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onNextClicked() {
        List<String> areaID = new ArrayList<String>();
        areaID = Arrays.asList(getResources().getStringArray(R.array.country_list_code));
        String area = "";
        if(selectedAreaID != -1){
            area = areaID.get(selectedAreaID);
        }
        model.setUserInformation(binding.editName.getText().toString(),
                    isEID, binding.editEID.getText().toString(),
                    binding.editPassport.getText().toString(),
                    binding.editMobile.getText().toString(),
                    binding.editEmail.getText().toString(),
                    lat, longitude, gender, area,
                    binding.editAge.getText().toString());
    }


    private void getNameByCoordinates(double lat, double lon) {
        this.lat = lat;
        this.longitude = lon;
        String address = "";
        try {
            mGeocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && addresses.size() > 0) {
                //return addresses.get(0);
                address =  addresses.get(0).getAddressLine(0);
            }
        } catch (IOException ex){

        }
        binding.editLocation.setText(address);
    }
}

