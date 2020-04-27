package com.android.dubaicovid19.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.databinding.FragmentPreventCovidBinding;
import com.android.dubaicovid19.view.viewModels.PreventCovidViewModel;

public class PreventCovidFragment extends Fragment {

    FragmentPreventCovidBinding binding;
    PreventCovidViewModel model;
    private View mRootView;

    public static PreventCovidFragment newInstance() {
        Bundle args = new Bundle();
        PreventCovidFragment fragment = new PreventCovidFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(PreventCovidViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prevent_covid, container, false);
        binding.setVmFragmentPreventCovid(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
