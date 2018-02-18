package com.tpoint.ui.MainActivity;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface MainActivityPresenter {

    // on activity onResume()
    void onResume();

    // on activity onPause()
    void onPause();

    // on activity onDestroy()
    void onDestroy();

    // called to init the location manager
    void startLocationTracking();

    // called to get all nearby points from firebase
    void getNearbyPoints();

    // called when user tagged a point
    void tagPoint();

    // called when user un-tagged a point
    void unTagPoint();

    // called when user changed channel
    void channelChange(int channel);
}
