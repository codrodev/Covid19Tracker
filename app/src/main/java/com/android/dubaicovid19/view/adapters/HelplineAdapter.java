package com.android.dubaicovid19.view.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dubaicovid19.BR;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.Helpline;
import com.android.dubaicovid19.view.viewModels.HelplineViewModel;

import java.util.List;

public class HelplineAdapter extends RecyclerView.Adapter<HelplineAdapter.GenericViewHolder> {
    private int layoutId;
    private List<Helpline> lstHelpline;
    private HelplineViewModel viewModel;
    Activity activity;

    public HelplineAdapter(@LayoutRes int layoutId, HelplineViewModel viewModel, Activity activity) {
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

        return new HelplineAdapter.GenericViewHolder(binding);
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
        return  lstHelpline.size();
    }

    public void setHelpline(List<Helpline> lstHelpline) {
        this.lstHelpline = lstHelpline;
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(HelplineViewModel viewModel, int position) {

            binding.setVariable(com.android.dubaicovid19.BR.vmAdapterHelpline, viewModel);
            binding.setVariable(com.android.dubaicovid19.BR.position, position);

            binding.executePendingBindings();

        }

        @Override
        public void onClick(View view) {
            openPhoneIntent(String.valueOf(lstHelpline.get(getAdapterPosition()).getPhoneNumber()));
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