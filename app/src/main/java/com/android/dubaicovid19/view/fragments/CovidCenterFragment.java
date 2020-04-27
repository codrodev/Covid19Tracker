package com.android.dubaicovid19.view.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.databinding.FragmentCovidCenterBinding;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.factories.CovidCenterFactory;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;

import java.util.List;

public class CovidCenterFragment extends Fragment implements CovidCenterNavigator {
    FragmentCovidCenterBinding binding;
    CovidCenterViewModel model;
    CovidCenterFactory factory;
    CovidCenterRepository repository;
    private View mRootView;


    public static CovidCenterFragment newInstance() {
        Bundle args = new Bundle();
        CovidCenterFragment fragment = new CovidCenterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new CovidCenterRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new CovidCenterFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(CovidCenterViewModel.class);
        model.navigator = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_center, container, false);
        binding.setVmFragmentCovidCenter(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.apiGetCovidCenter();

        binding.txtAll.setTextColor(getResources().getColor(R.color.white));
        binding.txtNearBy.setTextColor(getResources().getColor(R.color.stats_switch_on));

        binding.switchStatistics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    model.apiGetNearByCovidCenter();
                    binding.txtNearBy.setTextColor(getResources().getColor(R.color.white));
                    binding.txtAll.setTextColor(getResources().getColor(R.color.stats_switch_on));
                } else {
                    model.apiGetCovidCenter();
                    binding.txtAll.setTextColor(getResources().getColor(R.color.white));
                    binding.txtNearBy.setTextColor(getResources().getColor(R.color.stats_switch_on));
                }
            }
        });
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        initializeAdapter();
    }

    private void initializeAdapter(){
        model.getMutableCovidCenter().observe(getActivity(), new Observer<List<CovidCenters>>() {
            @Override
            public void onChanged(List<CovidCenters> lstCovidCenters) {
                //model.loading.set(View.GONE);
                if (lstCovidCenters.size() > 0) {
                    model.setCovidCenterAdapter(lstCovidCenters);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess() {
        //model.setCovidCenterAdapter(model.getAdapter());
        binding.recylerCovidCenter.setVisibility(View.VISIBLE);
        binding.recylerCovidCenter.setAdapter(model.getAdapter());

        //binding.emptyMessage.setText(getResources().getString(R.string.no_available_center));

    }

    @Override
    public void onEmpty(String Msg) {
        AlertDialogUtil.alertDialog(getResources().getString(R.string.no_available_center), getActivity());
    }

    @Override
    public void onFailure(String Msg) {
        AlertDialogUtil.alertDialog(Msg, getActivity());
    }
}
