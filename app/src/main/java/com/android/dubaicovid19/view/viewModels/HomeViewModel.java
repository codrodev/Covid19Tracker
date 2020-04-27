package com.android.dubaicovid19.view.viewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.data.model.GridMenuModel;
import com.android.dubaicovid19.data.model.ParentGridMenu;
import com.android.dubaicovid19.view.adapters.GridMenuAdapter;
import com.android.dubaicovid19.view.navigators.FragmentNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeViewModel extends ViewModel {

    FragmentNavigator frNavigator;
    private MutableLiveData<List<GridMenuModel>> mutableGridMenu = new MutableLiveData<>();
    private MutableLiveData<List<ParentGridMenu>> mutableParentGridMenu = new MutableLiveData<>();

    List<String> arrHomeMenu = new ArrayList<>();
    List<String> arrHomeMenuID = new ArrayList<>();
    List<GridMenuModel> lstGridMenu = new ArrayList<>();
    List<ParentGridMenu> lstParentGridMenu = new ArrayList<>();
    GridMenuAdapter adapter;
    int index = 0;

    public HomeViewModel(){

    }

    public void initializeVM(Activity activity, GridMenuAdapter.OnMenuSelectedListener listener){
        populateParentGridMenu(activity);
    }

    public MutableLiveData<List<GridMenuModel>> getMutableGridMenu() {
        return mutableGridMenu;
    }

    private void populateParentGridMenu(Activity activity){
        mutableParentGridMenu = new MutableLiveData<>();
        lstParentGridMenu = new ArrayList<>();

        arrHomeMenu =  Arrays.asList(activity.getResources().getStringArray(R.array.home_menu));
        arrHomeMenuID =  Arrays.asList(activity.getResources().getStringArray(R.array.home_menu_id));

        List<String> lst1 = arrHomeMenu.subList(0,arrHomeMenu.size());
        //List<String> lst2 = arrHomeMenu.subList(9,arrHomeMenu.size());

        List<String> lstID1 = arrHomeMenuID.subList(0,arrHomeMenuID.size());
        //List<String> lstID2 = arrHomeMenuID.subList(9,arrHomeMenuID.size());


        ParentGridMenu p1 = new ParentGridMenu();
        p1 = populateGridMenu(lst1, lstID1);

        lstParentGridMenu.add(p1);

        /*ParentGridMenu p2 = new ParentGridMenu();
        p2 = populateGridMenu(lst2, lstID2);

        lstParentGridMenu.add(p2);*/
        mutableParentGridMenu.setValue(lstParentGridMenu);
    }

    public List<GridMenuModel> getListHomeGridMenuItems(int position) {
        return mutableParentGridMenu.getValue().get(position).getLstGridMenu();
    }

    public void setMutableGridMenu(List<GridMenuModel> lstGridMenu){
        mutableGridMenu = new MutableLiveData<>();
        mutableGridMenu.setValue(lstGridMenu);
    }

    public int getGridPagerSize(){
        /*if(mutableHomeGridMenu.getValue().size() < 7){
            return 1;
        } else {
            return ((mutableHomeGridMenu.getValue().size() / 6) + 1);
        }*/
        return 1;
    }

    private ParentGridMenu populateGridMenu(List<String> arrMenu, List<String> arrMenuID){
        ParentGridMenu p1= new ParentGridMenu();
        arrHomeMenu = new ArrayList<>();
        lstGridMenu = new ArrayList<>();
        for (int i = 0; i < arrMenu.size(); i++){
            GridMenuModel obj = new GridMenuModel();
            obj.setItemId(Integer.parseInt(arrMenuID.get(i)));
            obj.setItemName(arrMenu.get(i));
            lstGridMenu.add(obj);
        }
        p1.setLstGridMenu(lstGridMenu);
       // mutableGridMenu.setValue(lstGridMenu);
        return p1;
    }

    public GridMenuAdapter getMenuAdapter() {
        return adapter;
    }

    public GridMenuModel getCurrentGridMenu(int position){
        if (mutableGridMenu.getValue() != null &&
                mutableGridMenu.getValue().size() > position) {
            return mutableGridMenu.getValue().get(position);
        }
        return null;
    }

    public void  setGridMenuAdapter(List<GridMenuModel> lstGridMenu) {
        this.adapter.notifyDataSetChanged();
    }

    public void navigate(Context ctx, String fragmentTag){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.fragmentNavigator(fragmentTag, true, null);
    }
}
