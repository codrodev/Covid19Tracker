package com.android.dubaicovid19.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.view.viewModels.HomeViewModel;

public class GridMenuPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Activity context;
    HomeViewModel model;
    GridMenuAdapter adapterGridMenu;
    GridMenuAdapter.OnMenuSelectedListener listner;

    public GridMenuPagerAdapter(Activity context, HomeViewModel model, GridMenuAdapter.OnMenuSelectedListener listner) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.model = model;
        this.listner = listner;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.pager_adapter_grid_menu, container, false);
        RecyclerView recycleGridMenu = (RecyclerView)view.findViewById(R.id.recycleGridMenu);
        recycleGridMenu.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        if(AppConstants.CURRENT_LOCALE.equals("en")) recycleGridMenu.setLayoutDirection(View.LAYOUT_DIRECTION_LTR); else recycleGridMenu.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        adapterGridMenu = new GridMenuAdapter(R.layout.adapter_grid_menu, model, context, listner, position);
        recycleGridMenu.setAdapter(adapterGridMenu);
        /*if(Global.isFirstLoad) {
            ViewAnimationUtils.scaleAnimateViewPop(recycleGridMenu);
        }*/
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return model.getGridPagerSize();
        //return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       //super.destroyItem((View) container, position, object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }
}
