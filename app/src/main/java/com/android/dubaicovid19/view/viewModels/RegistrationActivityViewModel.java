package com.android.dubaicovid19.view.viewModels;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.view.adapters.RegistrationPagerAdapter;

public class RegistrationActivityViewModel extends ViewModel {

    RegistrationPagerAdapter pagerAdapter;
    public RegistrationActivityViewModel(){

    }

    public RegistrationPagerAdapter getRegistrationPagerAdapter() {
        return pagerAdapter;
    }

    public void setRegistrationPagerAdapter(FragmentManager fr) {
        pagerAdapter = new RegistrationPagerAdapter(fr);
    }
}
