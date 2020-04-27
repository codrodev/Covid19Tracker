package com.android.dubaicovid19.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;


import androidx.appcompat.app.AppCompatActivity;

import com.android.dubaicovid19.CovidTrackerApp;
import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.Global;
import com.android.dubaicovid19.utility.constant.AppConstants;


public class LanguageActivity extends AppCompatActivity {


    @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_settings);

        Button btnEnglish = (Button) findViewById(R.id.btnEnglish);
        Button btnArabic = (Button) findViewById(R.id.btnArabic);
        final int uID = getSharedPreferences("MyPref", 0).getInt(AppConstants.USER_ID_KEY,-1);
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.changeLang("en", getApplicationContext());
                if(uID  > 0) {
                    Intent login = new Intent(LanguageActivity.this, MainActivity.class);
                    startActivity(login);
                } else {
                    Intent login = new Intent(LanguageActivity.this, RegistrationActivity.class);
                    startActivity(login);
                }
                finish();
            }
        });

        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.changeLang("ar", getApplicationContext());
                if(uID  > 0) {
                    Intent login = new Intent(LanguageActivity.this, MainActivity.class);
                    startActivity(login);
                } else {
                    Intent login = new Intent(LanguageActivity.this, RegistrationActivity.class);
                    startActivity(login);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login = new Intent(LanguageActivity.this,MainActivity.class);
        startActivity(login);
        finish();
    }
}
