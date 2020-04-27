package com.android.dubaicovid19.view.fragments;

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
import com.android.dubaicovid19.databinding.FragmentCovidInfoBinding;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.view.viewModels.CovidInfoViewModel;

public class CovidInfoFragment extends Fragment{

    CovidInfoViewModel model;
    FragmentCovidInfoBinding binding;
    private View mRootView;

    public static CovidInfoFragment newInstance() {
        Bundle args = new Bundle();
        CovidInfoFragment fragment = new CovidInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(CovidInfoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_info, container, false);
        binding.setVmFragmentCovidInfo(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        if(Global.isEdit){
            binding.btnCovid.setVisibility(View.VISIBLE);
        }
        model.setConvidInfo("SAF");
        binding.rbsuspected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    model.setConvidInfo("SSP");
                }
            }
        });
        binding.rbSafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    model.setConvidInfo("SAF");
                }
            }
        });
        binding.rbQuarantinFacility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    model.setConvidInfo("QIF");
                }
            }
        });
        binding.rbHomeQuarantined.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    model.setConvidInfo("HOQ");
                }
            }
        });
        binding.rbconfirmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    model.setConvidInfo("CNF");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

