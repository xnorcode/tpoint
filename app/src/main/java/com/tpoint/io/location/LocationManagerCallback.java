package com.tpoint.io.location;

import com.google.android.gms.common.api.Status;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface LocationManagerCallback {

    // triggered if there is a problem with the requirements
    // for running the location API from prompting resolution
    void onLocationResolution(Status status);

    // triggered when the location manager starts receiving
    // location data from the location API
    void onLocationTrackingStarted(boolean started);

}
