package com.android.dubaicovid19.view.adapters;

import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.dubaicovid19.view.fragments.CovidInfoFragment;
import com.android.dubaicovid19.view.fragments.UserRegistrationFragment;
import com.android.dubaicovid19.view.viewModels.RegistrationActivityViewModel;

public class RegistrationPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    RegistrationActivityViewModel model;
    public RegistrationPagerAdapter(FragmentManager fm){
        super(fm);
        this.model = model;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment tabFragment;
        switch (position) {
            case 0:
                tabFragment = UserRegistrationFragment.newInstance();
                break;
            case 1:
                tabFragment = CovidInfoFragment.newInstance();
                break;
            default:
                tabFragment = UserRegistrationFragment.newInstance();
                break;
        }
        return tabFragment;
    }

    @Override
    public int getCount() {
        return 2;
        /*if (tabs != null)
            return tabs.length;
        else
            return 0;*/
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        if (tabs != null && position < tabs.length)
            return tabs[position];
        else
            return "";
    }*/
}