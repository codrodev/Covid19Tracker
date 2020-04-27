package com.android.dubaicovid19.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.PostSelfAssessment;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.SelfAssessmentRepository;
import com.android.dubaicovid19.databinding.FragmentSelfAssessmentBinding;
import com.android.dubaicovid19.view.factories.SelfAssessmentFactory;
import com.android.dubaicovid19.view.viewModels.SelfAssessmentViewModel;

public class SelfAssessmentFragment extends Fragment {

    FragmentSelfAssessmentBinding binding;
    SelfAssessmentViewModel model;
    private View mRootView;
    SelfAssessmentFactory factory;
    SelfAssessmentRepository repository;
    PostSelfAssessment selfAssessment;

    public static SelfAssessmentFragment newInstance() {
        Bundle args = new Bundle();
        SelfAssessmentFragment fragment = new SelfAssessmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new SelfAssessmentRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new SelfAssessmentFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(SelfAssessmentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_self_assessment, container, false);
        binding.setVmFragmentSelfAssessment(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        selfAssessment = new PostSelfAssessment();
        binding.chipNo1.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
        binding.chipNo2.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
        binding.chipNo3.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
        binding.chipNo4.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));



        binding.chipYes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chipYes1.setTextColor(getResources().getColor(R.color.white));
                binding.chipNo1.setTextColor(getResources().getColor(R.color.black));

                binding.chipYes1.setChipIcon(getResources().getDrawable(R.drawable.ic_like_active));
                binding.chipNo1.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike));
                binding.chipYes1.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                binding.chipNo1.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                selfAssessment.setRecentlyTravelled(true);
            }
        });
        binding.chipNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chipYes1.setTextColor(getResources().getColor(R.color.black));
                binding.chipNo1.setTextColor(getResources().getColor(R.color.white));

                binding.chipYes1.setChipIcon(getResources().getDrawable(R.drawable.ic_like));
                binding.chipNo1.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike_active));
                binding.chipYes1.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                binding.chipNo1.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                selfAssessment.setRecentlyTravelled(false);
            }
        });

        binding.chipYes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setCameIntoCloseContact(true);
                binding.chipYes2.setTextColor(getResources().getColor(R.color.white));
                binding.chipNo2.setTextColor(getResources().getColor(R.color.black));

                binding.chipYes2.setChipIcon(getResources().getDrawable(R.drawable.ic_like_active));
                binding.chipNo2.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike));
                binding.chipYes2.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                binding.chipNo2.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
            }
        });
        binding.chipNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setCameIntoCloseContact(false);
                binding.chipYes2.setTextColor(getResources().getColor(R.color.black));
                binding.chipNo2.setTextColor(getResources().getColor(R.color.white));

                binding.chipYes2.setChipIcon(getResources().getDrawable(R.drawable.ic_like));
                binding.chipNo2.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike_active));
                binding.chipYes2.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                binding.chipNo2.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
            }
        });

        binding.chipYes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setHavingFever(true);
                binding.chipYes3.setTextColor(getResources().getColor(R.color.white));
                binding.chipNo3.setTextColor(getResources().getColor(R.color.black));

                binding.chipYes3.setChipIcon(getResources().getDrawable(R.drawable.ic_like_active));
                binding.chipNo3.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike));
                binding.chipYes3.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                binding.chipNo3.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
            }
        });
        binding.chipNo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setHavingFever(false);
                binding.chipYes3.setTextColor(getResources().getColor(R.color.black));
                binding.chipNo3.setTextColor(getResources().getColor(R.color.white));

                binding.chipYes3.setChipIcon(getResources().getDrawable(R.drawable.ic_like));
                binding.chipNo3.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike_active));
                binding.chipYes3.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                binding.chipNo3.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
            }
        });

        binding.chipYes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setFirstResponder(true);
                binding.chipYes4.setTextColor(getResources().getColor(R.color.white));
                binding.chipNo4.setTextColor(getResources().getColor(R.color.black));

                binding.chipYes4.setChipIcon(getResources().getDrawable(R.drawable.ic_like_active));
                binding.chipNo4.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike));
                binding.chipYes4.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                binding.chipNo4.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
            }
        });
        binding.chipNo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selfAssessment.setFirstResponder(false);
                binding.chipYes4.setTextColor(getResources().getColor(R.color.black));
                binding.chipNo4.setTextColor(getResources().getColor(R.color.white));

                binding.chipYes4.setChipIcon(getResources().getDrawable(R.drawable.ic_like));
                binding.chipNo4.setChipIcon(getResources().getDrawable(R.drawable.ic_dislike_active));
                binding.chipYes4.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                binding.chipNo4.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.apiSelfAssessment(selfAssessment);
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
    public void onResume() {
        super.onResume();
    }
}
