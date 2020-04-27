package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.Helpline;
import com.android.dubaicovid19.view.adapters.HelplineAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelplineViewModel extends ViewModel {

    public MutableLiveData<List<Helpline>> mutableHelpline = new MutableLiveData<>();
    Activity activity;
    HelplineAdapter adapter;

    public HelplineViewModel(){

    }

    public void initializeViewModel(Activity activity){
        this.activity = activity;
        List<String> helplineNAme = Arrays.asList(activity.getResources().getStringArray(R.array.help_line_name));
        List<String> helplineNumber = Arrays.asList(activity.getResources().getStringArray(R.array.help_line_number));
        populateHelpline(helplineNAme, helplineNumber);
    }

    private void populateHelpline(List<String> helplineNAme, List<String> helplineNumber){
        List<Helpline> lstHelpline = new ArrayList<>();
        if(helplineNAme != null && helplineNAme.size() > 0 && helplineNumber != null && helplineNumber.size() > 0){
            for (int i = 0; i < helplineNAme.size(); i++){
                Helpline obj = new Helpline();
                obj.setHelplineName(helplineNAme.get(i));
                obj.setPhoneNumber(helplineNumber.get(i));

                lstHelpline.add(obj);
            }
        }

        mutableHelpline.setValue(lstHelpline);
        setHelpLine(lstHelpline);
    }

    public void setHelpLine(List<Helpline> lstHelpline){
        adapter = new HelplineAdapter(R.layout.adapter_helpline, this, activity);
        setHelplineAdapter(lstHelpline);
    }

    public void  setHelplineAdapter(List<Helpline> lstHelpline) {
        this.adapter.setHelpline(lstHelpline);
        this.adapter.notifyDataSetChanged();
    }

    public HelplineAdapter getAdapter() {
        return adapter;
    }

    public Helpline getCurrentHelpline(int position){
        if (mutableHelpline.getValue() != null ) {
            return mutableHelpline.getValue().get(position);
        }
        return null;
    }

    public MutableLiveData<List<Helpline>> getMutableHelpline(){
        return mutableHelpline;
    }

}
