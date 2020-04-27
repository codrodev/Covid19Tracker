package com.android.dubaicovid19.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.CovidLogRepository;
import com.android.dubaicovid19.databinding.FragmentCovidLogBinding;
import com.android.dubaicovid19.utility.MapUtility;
import com.android.dubaicovid19.view.activities.LocationPickerActivity;
import com.android.dubaicovid19.view.factories.CovidLogFactory;
import com.android.dubaicovid19.view.viewModels.CovidLogViewModel;

public class CovidLogFragment extends Fragment {

    CovidLogViewModel model;
    FragmentCovidLogBinding binding;
    private View mRootView;
    CovidLogFactory factory;
    CovidLogRepository repository;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    double lat = -1, longitude = -1;
    boolean isEID;
    String result;

    public static CovidLogFragment newInstance() {
        Bundle args = new Bundle();
        CovidLogFragment fragment = new CovidLogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new CovidLogRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new CovidLogFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(CovidLogViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_log, container, false);
        binding.setVmFragmentCovidLog(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        isEID = true;

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

        result = "Positive";
        binding.rbPositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    result = "Positive";
                }
            }
        });

        binding.rbNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    result = "Negative";
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

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id;
                if(isEID){
                    id = binding.editEID.getText().toString();

                } else {
                    id = binding.editPassport.getText().toString();
                }
                if(model.isValidData(binding.editName.getText().toString(), id, isEID, lat, longitude, result, binding.editMobile.getText().toString())) {
                    model.apiUpdateCovidLog(binding.editName.getText().toString(), id, isEID, lat, longitude, result, binding.editMobile.getText().toString());
                }
            }
        });

        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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
}
