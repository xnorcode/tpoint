package com.tpoint.io.internet;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface InternetConnectivityCheckCallback {

    // triggered when internet check is completed
    void onInternetCheckCompleted(boolean connected);
}
