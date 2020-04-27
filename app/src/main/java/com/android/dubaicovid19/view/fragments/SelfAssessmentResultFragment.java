package com.android.dubaicovid19.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.view.navigators.FragmentNavigator;

public class SelfAssessmentResultFragment extends Fragment {

    private static String ARG_RECORD = "rec";
    private static String ARG_CODE = "code";
    private static String message, colorCode;
    FragmentNavigator navigator;

    public static SelfAssessmentResultFragment newInstance(String record, String code) {
        Bundle args = new Bundle();
        SelfAssessmentResultFragment fragment = new SelfAssessmentResultFragment();
        args.putString(ARG_RECORD, record);
        args.putString(ARG_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_RECORD);
            colorCode = getArguments().getString(ARG_CODE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selfassessment_result, container, false);
        Button btnAction = (Button) view.findViewById(R.id.btnActi);
        ImageView btnHome = (ImageView) view.findViewById(R.id.btnHome);
        TextView textReco = (TextView) view.findViewById(R.id.textReco);
        RelativeLayout layoutReco = (RelativeLayout) view.findViewById(R.id.layoutReco);
        if(message != null && message.length() > 0){
            textReco.setText(message);
        }

        if (colorCode != null && colorCode.length() > 0){
            layoutReco.setBackgroundColor(Color.parseColor(colorCode));
        }

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator = (FragmentNavigator) getActivity();
                navigator.fragmentNavigator(FragmentTAGS.FR_COVID_CENTER, true, null);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}