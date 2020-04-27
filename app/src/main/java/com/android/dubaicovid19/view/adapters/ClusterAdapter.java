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
import com.android.dubaicovid19.data.model.ContainmentCluster;
import com.android.dubaicovid19.data.model.Helpline;
import com.android.dubaicovid19.utility.constant.FragmentTAGS;
import com.android.dubaicovid19.view.activities.MainActivity;
import com.android.dubaicovid19.view.viewModels.ContainmentClusterViewModel;
import com.android.dubaicovid19.view.viewModels.HelplineViewModel;

import java.util.List;
import java.util.Locale;

public class ClusterAdapter extends RecyclerView.Adapter<ClusterAdapter.GenericViewHolder> {
    private int layoutId;
    private List<ContainmentCluster> lstCluster;
    private ContainmentClusterViewModel viewModel;
    Activity activity;

    public ClusterAdapter(@LayoutRes int layoutId, ContainmentClusterViewModel viewModel, Activity activity) {
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

        return new ClusterAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);

    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstCluster.size();
    }

    public void setContainmentCluster(List<ContainmentCluster> lstCluster) {
        this.lstCluster = lstCluster;
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(ContainmentClusterViewModel viewModel, int position) {

            binding.setVariable(com.android.dubaicovid19.BR.vmAdapterCluster, viewModel);
            binding.setVariable(com.android.dubaicovid19.BR.position, position);

            binding.executePendingBindings();

        }

        @Override
        public void onClick(View view) {
            /*String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lstCluster.get(getAdapterPosition()).getLat(),
                    lstCluster.get(getAdapterPosition()).getLon());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            activity.startActivity(intent);*/
            ((MainActivity)activity).loadFragment(FragmentTAGS.FR_CLUSTER_MAP, true,null);
        }
    }
}