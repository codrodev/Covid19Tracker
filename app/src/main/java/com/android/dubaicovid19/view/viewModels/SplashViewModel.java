package com.android.dubaicovid19.view.viewModels;


import com.android.dubaicovid19.view.navigators.SplashNavigator;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(){

    }

    public void startSeeding() {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                    decideNextActivity();

                } catch (InterruptedException e) {

                } finally {
                    decideNextActivity();
                }

            }
        };
        splashTread.start();

    }

    private void decideNextActivity() {
        getNavigator().openMainActivity();
    }

}
