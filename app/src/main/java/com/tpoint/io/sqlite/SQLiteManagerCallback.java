package com.tpoint.io.sqlite;

import com.tpoint.data.TPoint;

/**
 * Created by xnorcode on 18/02/2018.
 */

public interface SQLiteManagerCallback {

    // called when a point is saved in db
    void pointSaved(boolean saved);

    // called when list of points are saved in the db
    void allPointsSaved(boolean saved);

    // called when point was successfully found in the db
    void pointGet(TPoint point);

    // called when a point is updated in the db
    void pointUpdated(boolean updated);

    // called when a point is successfully deleted from the db
    void pointDeleted(boolean deleted);

    // called when all points are successfully deleted from the db
    void allPointsDeleted(boolean deleted);

    // called every time the scheduler checks the distances
    // returns:
    //          a) null; if there is no point within a 500m range
    //          b) the TPoint that is within a 500m range
    void schedulerAlertCheck(TPoint tp);

}
