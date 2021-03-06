package com.android.dubaicovid19.utility.constant;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public final class ViewAnimationUtils {

    private ViewAnimationUtils() {
        // This class is not publicly instantiable
    }

    public static void scaleAnimateView(View view) {
        ScaleAnimation animation =
                new ScaleAnimation(1f, 2f, 1f, 2f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

        view.setAnimation(animation);
        animation.setDuration(1500);
        animation.start();
    }

    public static void blinkAnimationView(View view){
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(800); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        //animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatCount(20); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);
        view.setAnimation(animation);
    }
}
