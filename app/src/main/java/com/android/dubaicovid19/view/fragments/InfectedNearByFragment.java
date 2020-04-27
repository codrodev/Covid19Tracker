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
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.InfectedNearByRepository;
import com.android.dubaicovid19.databinding.FragmentInfectedNearbyBinding;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.utility.constant.ViewAnimationUtils;
import com.android.dubaicovid19.view.activities.MainActivity;
import com.android.dubaicovid19.view.factories.InfectedNearByFActory;
import com.android.dubaicovid19.view.navigators.InfectedNearByNavigator;
import com.android.dubaicovid19.view.viewModels.InfectedNearByViewModel;

public class InfectedNearByFragment  extends Fragment implements InfectedNearByNavigator {
    FragmentInfectedNearbyBinding binding;
    InfectedNearByViewModel model;
    InfectedNearByFActory factory;
    InfectedNearByRepository repository;
    private View mRootView;


    public static InfectedNearByFragment newInstance() {
        Bundle args = new Bundle();
        InfectedNearByFragment fragment = new InfectedNearByFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new InfectedNearByRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new InfectedNearByFActory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(InfectedNearByViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_infected_nearby, container, false);
        binding.setVmFragmentInfectedNearBy(model);
        mRootView = binding.getRoot();
        model.navigator = this;
        initilizePage();

        return binding.getRoot();
    }

    private void initilizePage(){
        model.apiGetNearByInfectedUser();

        binding.btnShowOnMAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getContext(), LocationMarkerActivity.class);
                intent.putExtra(AppConstants.INFECTION_KEY, true);
                startActivity(intent);*/
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_MAP, true,null);
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
    public void onSuccess() {
        if(model.getInfectedUserCount() > 0){
            binding.btnShowOnMAp.setVisibility(View.VISIBLE);
        } else {
            binding.btnShowOnMAp.setVisibility(View.GONE);
        }
        ViewAnimationUtils.blinkAnimationView(binding.txtInfectedUser);
        binding.txtInfectedUser.setText(Integer.toString(model.getInfectedUserCount()));
    }
}
