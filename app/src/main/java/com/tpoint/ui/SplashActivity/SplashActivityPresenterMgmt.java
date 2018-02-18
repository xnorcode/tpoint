package com.tpoint.ui.SplashActivity;

import android.content.Context;

import com.tpoint.io.internet.InternetConnectivityCheck;
import com.tpoint.io.internet.InternetConnectivityCheckCallback;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class SplashActivityPresenterMgmt implements SplashActivityPresenter, InternetConnectivityCheckCallback {

    private SplashActivityView mView;
    private InternetConnectivityCheck internetConnectivityCheck;


    /*
    Constructor
     */
    public SplashActivityPresenterMgmt(SplashActivityView view) {
        this.mView = view;
    }


    @Override
    public void connectivityCheck(Context context) {
        // start connectivity check with internet followed by location check
        internetCheck(context);
    }


    @Override
    public void internetCheck(Context context) {
        // perform internet check
        internetConnectivityCheck = new InternetConnectivityCheck(context, this);
        internetConnectivityCheck.check();
    }


    @Override
    public void locationCheck() {
        // check location permissions
        mView.checkLocationPermission();
    }


    @Override
    public void proceed() {
        mView.nextActivity();
    }


    @Override
    public void onInternetCheckCompleted(boolean connected) {
        // alert user if no internet
        if (!connected) mView.noInternetAlert();
            // start location check
        else locationCheck();
    }


    public void destroy() {
        mView = null;
        if (internetConnectivityCheck != null) {
            internetConnectivityCheck.destroy();
        }
        internetConnectivityCheck = null;
    }
}
