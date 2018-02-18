package com.tpoint.ui.SplashActivity;

import android.content.Context;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface SplashActivityPresenter {

    // called to init connectivity check
    void connectivityCheck(Context context);

    // called to init internet connectivity check
    void internetCheck(Context context);

    // called to perform location connectivity check
    void locationCheck();

    // called to proceed to the next activity
    void proceed();
}
