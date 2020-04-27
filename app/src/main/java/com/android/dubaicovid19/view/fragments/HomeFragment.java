package com.android.dubaicovid19.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.GridMenuModel;
import com.android.dubaicovid19.databinding.FragmentHomeBinding;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.view.activities.BluetoothDetection;
import com.android.dubaicovid19.view.activities.LanguageActivity;
import com.android.dubaicovid19.view.activities.LocationPickerActivity;
import com.android.dubaicovid19.view.activities.MainActivity;
import com.android.dubaicovid19.view.activities.RiskActivity;
import com.android.dubaicovid19.view.adapters.GridMenuAdapter;
import com.android.dubaicovid19.view.adapters.GridMenuPagerAdapter;
import com.android.dubaicovid19.view.custom.CustPagerTransformer;
import com.android.dubaicovid19.view.custom.bluetooth.Config;
import com.android.dubaicovid19.view.viewModels.HomeViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements GridMenuAdapter.OnMenuSelectedListener, ViewPager.OnPageChangeListener {
    FragmentHomeBinding binding;
    HomeViewModel model;
    private View mRootView;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    private SharedPreferences sharedpreferences;
    private TextView[] dots;
    private GridMenuPagerAdapter gridPagerAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setVmFragmentHome(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.initializeVM(getActivity(), this);
        populateGridMenu();
        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getActivity(), LanguageActivity.class);
                startActivity(login);
                getActivity().finish();
            }
        });
    }

    public void populateGridMenu() {

        gridPagerAdapter = new GridMenuPagerAdapter(getActivity(),model, this);

        binding.viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setAdapter(gridPagerAdapter);
        /*if(model.getGridPagerSize() > 1) {
            binding.layoutDots.setVisibility(View.VISIBLE);
            addBottomDots(0, model.getGridPagerSize());
        } else {
            binding.layoutDots.setVisibility(View.GONE);
        }*/
        binding.layoutDots.setVisibility(View.GONE);
    }

    private void addBottomDots(int currentPage, int length) {
        dots = new TextView[length];

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dark_grey_shape));
            binding.layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.stats_switch_on));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMenuSelected(String menu, int position) {
        if(position == 0){
            Global.isBluetoothScan = false;
            Global.riskDevice = -1;
            Global.safeDevice = -1;
            Intent intent = new Intent(getActivity(), RiskActivity.class);
            getActivity().startActivity(intent);
        } else if(position == 1){
            model.navigate(getActivity(), FragmentTAGS.FR_INFECTED_NEARBY);
        }else if(position == 2){
            model.navigate(getActivity(), FragmentTAGS.FR_COVID_TRACKER);
        }else if(position == 3){
            model.navigate(getActivity(), FragmentTAGS.FR_COVID_CENTER);
        }else if(position == 4){
            model.navigate(getActivity(), FragmentTAGS.FR_PREVENT_COVID);
        }else if(position == 5){
            model.navigate(getActivity(), FragmentTAGS.FR_SELF_ASSESSMENT);
        }else if(position == 6){
            model.navigate(getActivity(), FragmentTAGS.FR_COVID_CLUSTER);
        }else if(position == 7){
            model.navigate(getActivity(), FragmentTAGS.FR_GOVT_ANNOUNCEMENT);
        }else if(position == 8){
            model.navigate(getActivity(), FragmentTAGS.FR_HELP_LINE);
        }else if(position == 9){
            model.navigate(getActivity(), FragmentTAGS.FR_COVID_TEST);
        } else if(position == 10){
            model.navigate(getActivity(), FragmentTAGS.FR_COVID_LOG);
        } else if(position == 11){
            model.navigate(getActivity(), FragmentTAGS.FR_TERMS);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position, model.getGridPagerSize());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
