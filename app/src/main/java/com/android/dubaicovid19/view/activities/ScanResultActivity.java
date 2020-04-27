package com.android.dubaicovid19.view.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.Global;

public class ScanResultActivity  extends AppCompatActivity {
    RelativeLayout layoutMAin;
    LinearLayout layoutCritical;
    LinearLayout riskCount;
    LinearLayout noRiskCount;
    TextView txtSafe;
    Button txtRiskStatus;
    TextView txtRisk;
    ImageView imgHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        layoutMAin = (RelativeLayout) findViewById(R.id.layoutMain);
        imgHome = (ImageView) findViewById(R.id.btnHome);
        layoutCritical = (LinearLayout) findViewById(R.id.layoutCritical);
        riskCount = (LinearLayout) findViewById(R.id.riskCount);
        noRiskCount = (LinearLayout) findViewById(R.id.noRiskCount);
        txtSafe = (TextView) findViewById(R.id.txtSafe);
        txtRisk = (TextView) findViewById(R.id.txtRisk);
        txtRiskStatus = (Button) findViewById(R.id.txtRiskStatus);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.isBluetoothScan = false;
                Global.riskDevice = -1;
                Global.safeDevice = -1;
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Global.isBluetoothScan) {
            if (Global.riskDevice > 0) {
                txtRiskStatus.setVisibility(View.VISIBLE);
                txtRiskStatus.setText(getResources().getString(R.string.risk));
                txtRiskStatus.setBackground(getResources().getDrawable(R.drawable.rounded_button_risk));
                layoutCritical.setVisibility(View.VISIBLE);
                riskCount.setVisibility(View.VISIBLE);
                noRiskCount.setVisibility(View.VISIBLE);
                int safe = Global.safeDevice;
                int risk = Global.riskDevice;
                txtSafe.setText(String.valueOf(safe));
                txtRisk.setText(String.valueOf(risk));


                layoutCritical.setBackground(getResources().getDrawable(R.drawable.layer_risk_circle));
            } else {
                txtRiskStatus.setVisibility(View.VISIBLE);
                txtRiskStatus.setText(getResources().getString(R.string.no_risk));
                txtRiskStatus.setBackground(getResources().getDrawable(R.drawable.rounded_button));
                riskCount.setVisibility(View.GONE);
                noRiskCount.setVisibility(View.VISIBLE);
                layoutCritical.setBackground(getResources().getDrawable(R.drawable.layer_no_risk_circle));
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Global.isBluetoothScan = false;
        Global.riskDevice = -1;
        Global.safeDevice = -1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Global.isBluetoothScan = false;
        Global.riskDevice = -1;
        Global.safeDevice = -1;
        finish();
    }
}
