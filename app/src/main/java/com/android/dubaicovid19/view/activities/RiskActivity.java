package com.android.dubaicovid19.view.activities;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.view.custom.bluetooth.BluetoothConfig;
import com.android.dubaicovid19.view.custom.bluetooth.Config;
import com.android.dubaicovid19.view.custom.bluetooth.IDetected;

public class RiskActivity extends AppCompatActivity implements IDetected {
    //LinearLayout layoutParent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothConfig.with(this)
                .setBackgroundColor(Color.parseColor("#000000"))
                .setPulseColor(Color.parseColor("#2380D3"))
                .setListener(this)
                .start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Global.isBluetoothScan == true){
            Intent intent = new Intent(RiskActivity.this, ScanResultActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onSelectedDevice(BluetoothDevice device) {

    }
}
