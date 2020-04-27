package com.android.dubaicovid19.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.CovidCenterRepository;
import com.android.dubaicovid19.data.repositories.CovidTestRepository;
import com.android.dubaicovid19.databinding.FragmentCovidTestBinding;
import com.android.dubaicovid19.view.factories.CovidCenterFactory;
import com.android.dubaicovid19.view.factories.CovidTestFactory;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;
import com.android.dubaicovid19.view.viewModels.CovidTestViewModel;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.List;

public class CovidTestFragment extends Fragment implements CovidCenterNavigator {
    CovidTestViewModel model;
    FragmentCovidTestBinding binding;
    CovidTestRepository repository;
    CovidTestFactory factory;
    private View mRootView;

    public static CovidTestFragment newInstance() {
        Bundle args = new Bundle();
        CovidTestFragment fragment = new CovidTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new CovidTestRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new CovidTestFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(CovidTestViewModel.class);
        model.navigator = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_test, container, false);
        binding.setVmFragmentCovidTest(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.apiGetCovidTestStatistics();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess() {
        model.getStatistics();

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Individuals", model.getStatistics().getTotalIndividualsTested()));
        data.add(new ValueDataEntry("Positive Cases", model.getStatistics().getTotalPositiveCases()));
        data.add(new ValueDataEntry("Total Samples", model.getStatistics().getTotalSamplesTested()));

        pie.data(data);

        pie.title("COVID Tests Result");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Date: " + model.getStatistics().getDate())
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        binding.pieChart.setChart(pie);
    }

    @Override
    public void onEmpty(String Msg) {

    }

    @Override
    public void onFailure(String Msg) {

    }
}
