package com.tpoint.ui.MainActivity;

import com.google.android.gms.common.api.Status;
import com.tpoint.data.TPoint;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface MainActivityView {

    // called when user tags a point
    void onPointTagged();

    // called when user untags a point
    void onPointUntagged();

    // called when user changes the channel
    void onChannelChangedAlert(int channel);

    // play alert when a point is closer than 500m
    void onPointNearbyAlert(TPoint tp);

    // finish activity
    void onTerminateActivity();

    // call for location resolution
    void onLocationResolutionRequest(Status status);
}
