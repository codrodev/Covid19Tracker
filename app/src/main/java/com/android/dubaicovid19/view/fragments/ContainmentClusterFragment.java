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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.ContainmentCluster;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.ContainmentClusterRepository;
import com.android.dubaicovid19.data.repositories.CovidLogRepository;
import com.android.dubaicovid19.databinding.FragmentContainmentClusterBinding;
import com.android.dubaicovid19.databinding.FragmentCovidLogBinding;
import com.android.dubaicovid19.utility.MapUtility;
import com.android.dubaicovid19.view.activities.LocationPickerActivity;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.factories.ContainmentClusterFactory;
import com.android.dubaicovid19.view.factories.CovidLogFactory;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.viewModels.ContainmentClusterViewModel;
import com.android.dubaicovid19.view.viewModels.CovidLogViewModel;

import java.util.List;

public class ContainmentClusterFragment extends Fragment implements CovidCenterNavigator {

    ContainmentClusterViewModel model;
    FragmentContainmentClusterBinding binding;
    private View mRootView;
    ContainmentClusterFactory factory;
    ContainmentClusterRepository repository;

    public static ContainmentClusterFragment newInstance() {
        Bundle args = new Bundle();
        ContainmentClusterFragment fragment = new ContainmentClusterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new ContainmentClusterRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new ContainmentClusterFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(ContainmentClusterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_containment_cluster, container, false);
        binding.setVmFragmentContainmentCluster(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.navigator = this;
        model.apiGetContainmentCluster();

        binding.txtAll.setTextColor(getResources().getColor(R.color.white));
        binding.txtNearBy.setTextColor(getResources().getColor(R.color.stats_switch_on));

        binding.switchStatistics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    model.apiGetNearByContainmentCluster();
                    binding.txtNearBy.setTextColor(getResources().getColor(R.color.white));
                    binding.txtAll.setTextColor(getResources().getColor(R.color.stats_switch_on));
                } else {
                    model.apiGetContainmentCluster();
                    binding.txtAll.setTextColor(getResources().getColor(R.color.white));
                    binding.txtNearBy.setTextColor(getResources().getColor(R.color.stats_switch_on));
                }
            }
        });

        initializeAdapter();
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initializeAdapter(){
        model.getMutableCluster().observe(getActivity(), new Observer<List<ContainmentCluster>>() {
            @Override
            public void onChanged(List<ContainmentCluster> lstCluster) {
                //model.loading.set(View.GONE);
                if (lstCluster.size() > 0) {
                    model.setClusterAdapter(lstCluster);
                }
            }
        });
    }

    @Override
    public void onSuccess() {
        binding.recyclerCluster.setVisibility(View.VISIBLE);
        binding.recyclerCluster.setAdapter(model.getAdapter());
    }

    @Override
    public void onEmpty(String Msg) {
        AlertDialogUtil.alertDialog(getResources().getString(R.string.no_available_cluster), getActivity());
    }

    @Override
    public void onFailure(String Msg) {

    }
}
