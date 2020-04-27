package com.android.dubaicovid19.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.databinding.ActivityMainBinding;
import com.android.dubaicovid19.databinding.ActivityRegistrationBinding;
import com.android.dubaicovid19.view.fragments.RegistrationParentFragment;
import com.android.dubaicovid19.view.viewModels.MainViewModel;
import com.android.dubaicovid19.view.viewModels.RegistrationActivityViewModel;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    ActivityRegistrationBinding binding;
    RegistrationActivityViewModel model;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadFragment();
    }

    public Fragment loadFragment() {
        fragmentManager = getSupportFragmentManager();
        tx = fragmentManager.beginTransaction();

        Fragment fragment = RegistrationParentFragment.newInstance();
        tx.replace(R.id.ui_container, fragment, "REGISTRATION");


        tx.commitAllowingStateLoss();
        return fragment;
    }

}
