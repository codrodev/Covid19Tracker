package com.android.dubaicovid19.view.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dubaicovid19.BR;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.CovidCenters;
import com.android.dubaicovid19.view.viewModels.CovidCenterViewModel;

import java.util.List;
import java.util.Locale;

public class CovidCenterAdapter extends RecyclerView.Adapter<CovidCenterAdapter.GenericViewHolder> {
    private int layoutId;
    private List<CovidCenters> lstCovidCenters;
    private CovidCenterViewModel viewModel;
    Activity activity;

    public CovidCenterAdapter(@LayoutRes int layoutId, CovidCenterViewModel viewModel, Activity activity) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.activity = activity;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new CovidCenterAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        //viewModel.initBase(position);
        //iewModel.setInterestListInAdapter(viewModel.getMutableFlightInfo().getValue());
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstCovidCenters.size();
    }

    public void setCovidCenterInfo(List<CovidCenters> lstCovidCenters) {
        this.lstCovidCenters = lstCovidCenters;
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.getRoot().findViewById(R.id.imgLocation).setOnClickListener(this);
            this.binding.getRoot().findViewById(R.id.imgCall).setOnClickListener(this);
        }

        void bind(CovidCenterViewModel viewModel, int position) {
           /* binding.setVariable(BR.pckDetailAdapterModel, viewModel);
            binding.setVariable(BR.position, position);*/

            binding.setVariable(BR.vmAdapterCovidCenter, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();

        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.imgLocation){
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lstCovidCenters.get(getAdapterPosition()).getLat(),
                        lstCovidCenters.get(getAdapterPosition()).getLon());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(intent);
            } else if(view.getId() == R.id.imgCall){
                openPhoneIntent(String.valueOf(lstCovidCenters.get(getAdapterPosition()).getContact()));
            }
        }

        public void openPhoneIntent(String phoneNumber) {

            Intent phIntent = new Intent(Intent.ACTION_DIAL);
            phIntent.setData(Uri.parse("tel:" + phoneNumber));
            try {
                activity.startActivity(Intent.createChooser(phIntent, null));
            } catch (ActivityNotFoundException ex) {

            }
        }


    }
}