package com.android.dubaicovid19.data.model;

import androidx.databinding.BaseObservable;

public class GridMenuModel extends BaseObservable {
    private int itemId;
    private String itemName;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
