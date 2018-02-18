package com.tpoint.ui.MainActivity;

import android.content.Context;

import com.google.android.gms.common.api.Status;
import com.tpoint.data.TPoint;
import com.tpoint.io.firebase.FirebaseHelper;
import com.tpoint.io.firebase.FirebaseHelperCallback;
import com.tpoint.io.location.LocationManager;
import com.tpoint.io.location.LocationManagerCallback;
import com.tpoint.io.sqlite.SQLiteManager;
import com.tpoint.io.sqlite.SQLiteManagerCallback;
import com.tpoint.utils.Engine;

import static com.tpoint.utils.Engine.isVisible;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class MainActivityPresenterMgmt implements MainActivityPresenter,
        FirebaseHelperCallback, LocationManagerCallback, SQLiteManagerCallback {

    private MainActivityView mView;
    private FirebaseHelper mFirebase;
    private LocationManager mLocation;
    private SQLiteManager mSQLite;
    private Context mContext;

    public MainActivityPresenterMgmt(Context context, MainActivityView view) {
        // save reference to the activity context
        this.mContext = context;
        // init view callback
        this.mView = view;
        // init firebase manager
        mFirebase = new FirebaseHelper(this);
        // init location manager
        mLocation = new LocationManager(this);
        // init SQLite manager
        mSQLite = new SQLiteManager(this);
    }


    /*
    Presenter Callback Methods
    */
    @Override
    public void onResume() {
        // keep app visibility status
        Engine.activityResumed();
    }

    @Override
    public void onPause() {
        // keep app visibility status
        Engine.activityPaused();
    }

    @Override
    public void onDestroy() {
        // release memory references
        if (mFirebase != null) {
            mFirebase.destroy();
        }
        mFirebase = null;
        if (mLocation != null) {
            mLocation.destroy();
        }
        mLocation = null;
        if (mSQLite != null) {
            // remove all local points
            mSQLite.deleteAllPoints(mContext, true);
            mSQLite.destroy();
        }
        mSQLite = null;
        mView = null;
        mContext = null;
    }

    @Override
    public void startLocationTracking() {
        // start location tracking
        mLocation.connect(mContext);
    }

    @Override
    public void getNearbyPoints() {
        TPoint point = mLocation.getCurrentPoint();
        // set current center point
        if (mFirebase.setCenterPoint(point)) {
            // remove all points from SQLite
            mSQLite.deleteAllPoints(mContext, true);
            // download all nearby points from firebase;
            // this will also download points that this
            // user has also tagged nearby
            mFirebase.getPointsNearby(point);
            // start a scheduler task that will check
            // distances every 10 sec and alert if necessary
            mSQLite.enablePointCheckScheduler(mContext, point);
            // TODO: 18/02/2018 handle enablePointCheckScheduler() according to the visibility status of the app.
        }
    }

    @Override
    public void tagPoint() {
        // get point from Location Manager
        TPoint point = mLocation.getCurrentPoint();
        // save point into SQLite
        mSQLite.savePoint(mContext, point);
        // save point into firebase
        mFirebase.savePoint(point);
        // inform user point is saved
        mView.onPointTagged();
    }

    @Override
    public void unTagPoint() {
        // get location from location manager
        TPoint point = mLocation.getCurrentPoint();
        // remove point in SQLite
        mSQLite.deletePoint(mContext, point);
        // remove point in firebase
        mFirebase.removePoint(point);
        // inform user point is removed
        mView.onPointUntagged();
    }

    @Override
    public void channelChange(int channel) {
        // set channel in firebase
        mFirebase.setChannel(channel);
        // inform user channel is changed
        mView.onChannelChangedAlert(channel);
        // TODO: 18/02/2018 clear other channel's points from cache
    }


    /*
    Firebase Callback Methods
     */
    @Override
    public void onPointTagged(boolean tagged) {
        // inform user
        mView.onPointTagged();
    }

    @Override
    public void onPointRemoved(TPoint tp) {
        // inform user
        mView.onPointUntagged();
    }

    @Override
    public void onPointFound(TPoint tp) {
        // save points in SQLite
        mSQLite.savePoint(mContext, tp);
        // perform check and alert user if point less tha 500m close
        if (tp.getDistance() < 500 && isVisible()) {
            mView.onPointNearbyAlert(tp);
        }
    }


    /*
        Location Manager Callback Methods
        */
    @Override
    public void onLocationResolution(Status status) {
        mView.onLocationResolutionRequest(status);
    }

    @Override
    public void onLocationTrackingStarted(boolean started) {
        if (started) {
            // download all points nearby from firebase
            getNearbyPoints();
        }
    }


    /*
    SQLite Manager Callback Methods
     */
    @Override
    public void pointSaved(boolean saved) {

    }

    @Override
    public void allPointsSaved(boolean saved) {

    }

    @Override
    public void pointGet(TPoint point) {

    }

    @Override
    public void pointUpdated(boolean updated) {

    }

    @Override
    public void pointDeleted(boolean deleted) {

    }

    @Override
    public void allPointsDeleted(boolean deleted) {

    }

    @Override
    public void schedulerAlertCheck(TPoint tp) {
        // alert user if a TPoint is found within 500m range
        if (tp != null && isVisible()) {
            mView.onPointNearbyAlert(tp);
        }
    }
}
