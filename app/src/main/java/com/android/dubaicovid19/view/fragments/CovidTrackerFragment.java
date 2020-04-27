package com.android.dubaicovid19.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.ChartData;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.data.network.ApiFactory;
import com.android.dubaicovid19.data.network.NetworkConnectionInterceptor;
import com.android.dubaicovid19.data.repositories.CovidTrackerRepository;
import com.android.dubaicovid19.databinding.FragmentCovidTrackerBinding;
import com.android.dubaicovid19.view.factories.CovidTrackerFactory;
import com.android.dubaicovid19.view.navigators.CovidCenterNavigator;
import com.android.dubaicovid19.view.navigators.CovidTrackerNavigator;
import com.android.dubaicovid19.view.viewModels.CovidTrackerViewModel;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Area;
import com.anychart.core.ui.Crosshair;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

public class CovidTrackerFragment extends Fragment implements CovidTrackerNavigator {
    FragmentCovidTrackerBinding binding;
    CovidTrackerViewModel model;
    CovidTrackerFactory factory;
    CovidTrackerRepository repository;
    private View mRootView;


    public static CovidTrackerFragment newInstance() {
        Bundle args = new Bundle();
        CovidTrackerFragment fragment = new CovidTrackerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new CovidTrackerRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new CovidTrackerFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(CovidTrackerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_tracker, container, false);
        binding.setVmFragmentCovidTracker(model);
        mRootView = binding.getRoot();
        initializePage();
        model.navigator = this;
        return binding.getRoot();
    }

    private void initializePage() {
        model.apiGetCovidLiveData(getResources().getString(R.string.uae));
        //model.apiChartData(getResources().getString(R.string.uae));


        binding.txtUAE.setTextColor(getResources().getColor(R.color.white));
        binding.txtWorld.setTextColor(getResources().getColor(R.color.stats_switch_on));

        binding.switchStatistics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    model.apiGetCovidLiveData(getResources().getString(R.string.world));
                    //model.apiChartData(getResources().getString(R.string.world));
                    binding.txtWorld.setTextColor(getResources().getColor(R.color.white));
                    binding.txtUAE.setTextColor(getResources().getColor(R.color.stats_switch_on));
                } else {
                    model.apiGetCovidLiveData(getResources().getString(R.string.uae));
                    //model.apiChartData(getResources().getString(R.string.uae));
                    binding.txtUAE.setTextColor(getResources().getColor(R.color.white));
                    binding.txtWorld.setTextColor(getResources().getColor(R.color.stats_switch_on));
                }
            }
        });
        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                model.apiGetCovidLiveData(binding.switchStatistics.isChecked() ?
                        getResources().getString(R.string.world) : getResources().getString(R.string.uae));
            }
        });
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        /*binding.chipUAE.setTextColor(getResources().getColor(R.color.white));
        binding.chipUAE.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));

        binding.chipUAE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.apiGetCovidLiveData();
                binding.chipUAE.setTextColor(getResources().getColor(R.color.white));
                binding.chipWorld.setTextColor(getResources().getColor(R.color.black));

                binding.chipUAE.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
                binding.chipWorld.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
            }
        });
        binding.chipWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //model.apiGetNearByCovidCenter();
                binding.chipUAE.setTextColor(getResources().getColor(R.color.black));
                binding.chipWorld.setTextColor(getResources().getColor(R.color.white));


                binding.chipUAE.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_in_active));
                binding.chipWorld.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_active));
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess() {
        binding.txtConfirm.setText(model.getConfirm());
        binding.txtActive.setText(model.getActive());
        binding.txtRecovered.setText(model.getRecovered());
        binding.txtDeceased.setText(model.getDeceased());
    }

    @Override
    public void onChartSuccess(List<ChartData> lstChart, String type) {
        //binding.areaChart.clear();
        Cartesian areaChart = AnyChart.area();
        areaChart.animation(true);

        Crosshair crosshair = areaChart.crosshair();
        crosshair.enabled(true);
        // TODO yStroke xStroke in crosshair
        crosshair.yStroke((Stroke) null, null, null, (String) null, (String) null)
                .xStroke("#0eb362", 1d, null, (String) null, (String) null)
                .zIndex(39d);
        crosshair.yLabel(0).enabled(true);

        areaChart.yScale().stackMode(ScaleStackMode.VALUE);

        List<DataEntry> seriesData = new ArrayList<>();
        if(lstChart != null && lstChart.size() > 0){
            for(int i = lstChart.size() - 1; i >= 0; i--){
                seriesData.add(new CustomDataEntry(lstChart.get(i).Date, lstChart.get(i).getConfirmed()));
            }
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

        Area series1 = areaChart.area(series1Data);
        series1.name("active cases");
        series1.stroke("#0eb362");
        series1.hovered().stroke("#0eb362");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d)
                .stroke("#0eb362");
        series1.markers().zIndex(100d);

        areaChart.legend().enabled(true);
        areaChart.legend().fontSize(13d);
        areaChart.legend().padding(0d, 0d, 20d, 0d);

        areaChart.xAxis(0).title("date");
        areaChart.yAxis(0).title("count");

        areaChart.interactivity().hoverMode(HoverMode.BY_X);
        areaChart.tooltip()
                .valuePrefix("cnt.")
                .displayMode(TooltipDisplayMode.UNION);
        binding.areaChart.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
        binding.areaChart.setBackgroundColor("#0eb362");
        binding.areaChart.setChart(areaChart);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}