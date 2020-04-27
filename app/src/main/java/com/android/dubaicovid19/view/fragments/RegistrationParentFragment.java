package com.android.dubaicovid19.view.fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.RegisterRepository;
import com.android.dubaicovid19.databinding.FragmentRegistrationParentBinding;
import com.android.dubaicovid19.view.factories.RegistrationParentFactory;
import com.android.dubaicovid19.view.viewModels.RegistrationParentFragmentViewModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RegistrationParentFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private View mRootView;
    RegistrationParentFragmentViewModel model;
    FragmentRegistrationParentBinding binding;
    RegistrationParentFactory factory;
    RegisterRepository repository;
    private TextView[] dots;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    public static onNextListner listner;

    public static RegistrationParentFragment newInstance() {
        Bundle args = new Bundle();
        RegistrationParentFragment fragment = new RegistrationParentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            repository = new RegisterRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new RegistrationParentFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(RegistrationParentFragmentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration_parent, container, false);
        binding.setVmFragmentRegistrationParent(model);
        mRootView = binding.getRoot();
        model.enableAllPermission();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.setRegistrationPagerAdapter(this);
        binding.viewPagerRegistration.addOnPageChangeListener(this);
        binding.viewPagerRegistration.setAdapter(model.getRegistrationPagerAdapter());
        setButtonVisibility(0);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onNextClicked();
                binding.viewPagerRegistration.setCurrentItem(getNext(), true);
            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPagerRegistration.setCurrentItem(getPrevious(), true);
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!model.isBluetoothEnabled()){
                    model.enableAllPermission();
                } else {
                    if (model.isValidData()) {
                        model.apiRegisterUser();
                    }
                }
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //addBottomDots(position);
        setButtonVisibility(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setButtonVisibility(int position){
        if(position == 0){
            binding.btnPrevious.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.btnRegister.setVisibility(View.GONE);
        } else if(position == 1){
            binding.btnPrevious.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private int getNext() {
        return binding.viewPagerRegistration.getCurrentItem() + 1;
    }

    private int getPrevious() {
        return binding.viewPagerRegistration.getCurrentItem() - 1;
    }


   /* private void addBottomDots(int currentPage) {
        dots = new TextView[2];

        int colorsActive = getResources().getColor(R.color.orange);
        int colorsInActive = getResources().getColor(R.color.white);

        binding.pageIndicator.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInActive);
            binding.pageIndicator.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive);
    }*/
   public interface onNextListner{
       public void onNextClicked();
   }
}
