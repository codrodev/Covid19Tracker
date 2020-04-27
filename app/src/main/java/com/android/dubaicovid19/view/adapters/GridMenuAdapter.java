package com.android.dubaicovid19.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dubaicovid19.BR;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.GridMenuModel;
import com.android.dubaicovid19.view.viewModels.HomeViewModel;

import java.util.List;

public class GridMenuAdapter extends RecyclerView.Adapter<GridMenuAdapter.GenericViewHolder> {

    private int layoutId;
    private List<GridMenuModel> lstMEnu;
    private HomeViewModel viewModel;
    Activity activity;
    OnMenuSelectedListener listener;

    public GridMenuAdapter(@LayoutRes int layoutId, HomeViewModel viewModel, Activity activity, OnMenuSelectedListener listener, int parentPosition) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.activity = activity;
        lstMEnu = viewModel.getListHomeGridMenuItems(parentPosition);
        viewModel.setMutableGridMenu(lstMEnu);
        this.listener = listener;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new GridMenuAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        holder.txtMenu.setText(lstMEnu.get(position).getItemName());
        if(lstMEnu.get(position).getItemId() == 0){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.risk_status));
        } else if(lstMEnu.get(position).getItemId() == 1){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.infected_nearby));
        }else if(lstMEnu.get(position).getItemId() == 2){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.statistics));
        }else if(lstMEnu.get(position).getItemId() == 3){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.hospital));
        }else if(lstMEnu.get(position).getItemId() == 4){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prevention));
        }else if(lstMEnu.get(position).getItemId() == 5){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.assessment));
        }else if(lstMEnu.get(position).getItemId() == 6){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.cluster));
        }else if(lstMEnu.get(position).getItemId() == 7){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.announcement));
        }else if(lstMEnu.get(position).getItemId() == 8){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.helpcenter));
        }else if(lstMEnu.get(position).getItemId() == 9){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.test_result));
        }else if(lstMEnu.get(position).getItemId() == 10){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.log_test));
        }else if(lstMEnu.get(position).getItemId() == 11){
            holder.imgIcon.setImageDrawable(activity.getResources().getDrawable(R.mipmap.terms));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstMEnu.size();
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        final ImageView imgIcon;
        final TextView txtMenu;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            imgIcon = (ImageView) this.binding.getRoot().findViewById(R.id.imgMenuIcon);
            txtMenu = (TextView) this.binding.getRoot().findViewById(R.id.txtMenuName);
            //this.binding.getRoot().setOnClickListener(this);
        }

        void bind(HomeViewModel viewModel, Integer position) {
           /* binding.setVariable(BR.vmAdapterGridMenu, viewModel);
            binding.setVariable(BR.position, position);*/

            binding.executePendingBindings();

        }

        @Override
        public void onClick(View v) {
            int id = lstMEnu.get(getAdapterPosition()).getItemId();
            listener.onMenuSelected(((TextView) binding.getRoot().findViewById(R.id.txtMenuName)).getText().toString(), id);
        }
    }

    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu, int position);
    }
}