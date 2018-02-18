package com.tpoint.io.firebase;

import com.tpoint.data.TPoint;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface FirebaseHelperCallback {

    // called when a point is saved in firebase
    void onPointTagged(boolean tagged);

    // called when a point is removed from firebase
    void onPointRemoved(TPoint tp);

    // called when a point is found/downloaded from firebase
    void onPointFound(TPoint tp);
}
