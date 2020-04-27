package com.android.dubaicovid19.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.dubaicovid19.R;
import com.android.dubaicovid19.utility.constant.AppConstants;
import com.android.dubaicovid19.utility.constant.ViewAnimationUtils;
import com.android.dubaicovid19.view.navigators.SplashNavigator;
import com.android.dubaicovid19.view.viewModels.SplashViewModel;

public class SplashActivity extends AppCompatActivity implements SplashNavigator {

    /*ActivitySplashBinding binding;
    */
    private SplashViewModel mSplashViewModel;
    /*@Override
    public void openIntroActivity() {
        Intent intent = IntroActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }*/

    @Override
    public void openMainActivity() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int userID = pref.getInt(AppConstants.USER_ID_KEY, -1);
        if(userID != -1){
            Intent intent = MainActivity.newIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        } else {
            //Intent login = new Intent(SplashActivity.this, LanguageActivity.class);
            Intent intent = RegistrationActivity.newIntent(SplashActivity.this);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashViewModel = new SplashViewModel();
        initializePage();
        mSplashViewModel.setNavigator(this);
        mSplashViewModel.startSeeding();
    }

    private void initializePage(){
        /*ViewAnimationUtils.scaleAnimateView(imgCovid);
        ViewAnimationUtils.scaleAnimateView(imgDubaiPolice);*/
    }
}
