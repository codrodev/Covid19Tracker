package com.android.dubaicovid19.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.Announcements;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.GovtAnnouncementRepository;
import com.android.dubaicovid19.databinding.FragmentGovtAnnoucementBinding;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.custom.AlertDialogUtil;
import com.android.dubaicovid19.view.factories.GovtAnnouncementFactory;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.viewModels.GovtAnnouncementViewModel;

import java.util.List;

import static com.android.dubaicovid19.view.custom.bluetooth.controller.BluetoothController.MY_PERMISSIONS_REQUEST_Location;

public class GovtAnnouncementFragment extends Fragment implements CovidCenterNavigator {
    FragmentGovtAnnoucementBinding binding;
    GovtAnnouncementViewModel model;
    GovtAnnouncementFactory factory;
    GovtAnnouncementRepository repository;
    private View mRootView;


    public static GovtAnnouncementFragment newInstance() {
        Bundle args = new Bundle();
        GovtAnnouncementFragment fragment = new GovtAnnouncementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new GovtAnnouncementRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new GovtAnnouncementFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(GovtAnnouncementViewModel.class);
        model.navigator = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_govt_annoucement, container, false);
        binding.setVmFragmentGovtAnnoucement(model);
        mRootView = binding.getRoot();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(), AppConstants.ALL_PERMISSIONS, MY_PERMISSIONS_REQUEST_Location);
        }
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.apiGetGovtAnnouncement();
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        initializeAdapter();
    }

    private void initializeAdapter(){
        model.getMutableAnnouncements().observe(getActivity(), new Observer<List<Announcements>>() {
            @Override
            public void onChanged(List<Announcements> lstAnnouncements) {
                //model.loading.set(View.GONE);
                if (lstAnnouncements.size() > 0) {
                    model.setAnnouncementAdapter(lstAnnouncements);
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
        binding.recylerAnnouncement.setVisibility(View.VISIBLE);
        binding.recylerAnnouncement.setAdapter(model.getAdapter());

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
