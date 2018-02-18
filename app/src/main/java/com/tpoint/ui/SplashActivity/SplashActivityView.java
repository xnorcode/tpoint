package com.tpoint.ui.SplashActivity;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface SplashActivityView {

    // triggered to start a no internet alert
    void noInternetAlert();

    // triggered to check for location permissions
    void checkLocationPermission();

    // triggered to go to the next activity
    void nextActivity();
}
