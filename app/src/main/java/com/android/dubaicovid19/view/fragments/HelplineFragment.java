package com.android.dubaicovid19.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.Helpline;
import com.android.dubaicovid19.databinding.FragmentHelplineBinding;
import com.android.dubaicovid19.view.viewModels.HelplineViewModel;

import java.util.List;

public class HelplineFragment extends Fragment {
    FragmentHelplineBinding binding;
    HelplineViewModel model;
    private View mRootView;


    public static HelplineFragment newInstance() {
        Bundle args = new Bundle();
        HelplineFragment fragment = new HelplineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(HelplineViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_helpline, container, false);
        binding.setVmFragmentHelpline(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.initializeViewModel(getActivity());
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        initializeAdapter();
    }

    private void initializeAdapter(){
        model.getMutableHelpline().observe(getActivity(), new Observer<List<Helpline>>() {
            @Override
            public void onChanged(List<Helpline> lstHelpline) {
                if (lstHelpline.size() > 0) {
                    model.setHelpLine(lstHelpline);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
