package com.android.dubaicovid19.view.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.dubaicovid19.data.model.Announcements;
import com.android.dubaicovid19.view.custom.OnlineImage.ImageLoader;
import com.android.dubaicovid19.view.viewModels.GovtAnnouncementViewModel;

import java.net.URL;
import java.util.List;
import java.util.Locale;

public class GovtAnnouncementAdapter extends RecyclerView.Adapter<GovtAnnouncementAdapter.GenericViewHolder> {
    private int layoutId;
    private List<Announcements> lstAnnouncements;
    private GovtAnnouncementViewModel viewModel;
    Activity activity;

    public GovtAnnouncementAdapter(@LayoutRes int layoutId, GovtAnnouncementViewModel viewModel, Activity activity) {
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

        return new GovtAnnouncementAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);

        /*try {
            URL url = new URL(lstAnnouncements.get(position).UrlToImage);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.imgAnnouncement.setImageBitmap(bmp);
        } catch (Exception ex){

        }*/
        int loader = R.drawable.ic_call;
        ImageLoader imgLoader = new ImageLoader(activity);
        imgLoader.DisplayImage(lstAnnouncements.get(position).UrlToImage, loader, holder.imgAnnouncement);
        //viewModel.initBase(position);
        //iewModel.setInterestListInAdapter(viewModel.getMutableFlightInfo().getValue());
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstAnnouncements.size();
    }

    public void setAnnouncements(List<Announcements> lstAnnouncements) {
        this.lstAnnouncements = lstAnnouncements;
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ViewDataBinding binding;
        final ImageView imgAnnouncement;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            imgAnnouncement = (ImageView) this.binding.getRoot().findViewById(R.id.imfGovtAnnouncement);
        }

        void bind(GovtAnnouncementViewModel viewModel, int position) {

            binding.setVariable(com.android.dubaicovid19.BR.vmAdapterGovtAnnouncement, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();

        }

        @Override
        public void onClick(View view) {
            /* String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lstCovidCenters.get(getAdapterPosition()).getLat(),
                        lstCovidCenters.get(getAdapterPosition()).getLon());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(intent);*/
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