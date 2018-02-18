package com.tpoint.io.firebase;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tpoint.data.TPoint;
import com.tpoint.utils.Tools;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class FirebaseHelper {


    // Firebase db ref
    private DatabaseReference mDatabaseRootRef;
    // Geofire ref
    private GeoFire mGeoFire;
    // Geofire query ref
    private GeoQuery mGeoQuery;
    // Geofire query callback ref
    private GeoQueryEventListener mGeoListener;
    // Firebase Helper Callback
    private FirebaseHelperCallback mFirebaseResponse;
    // Channel ref
    private int mChannel;
    // current center point ref
    private TPoint mCurrentCenter;


    /*
    Constructor
     */
    public FirebaseHelper(FirebaseHelperCallback response) {
        this.mFirebaseResponse = response;
        // set default channel at number 1
        setChannel(1);
    }


    /*
    Set channel
     */
    public void setChannel(int ch) {
        mChannel = ch;
        mDatabaseRootRef = FirebaseDatabase.getInstance().getReference("tpoints/" + mChannel);
        mGeoFire = new GeoFire(mDatabaseRootRef);
    }


    /*
    Get current channel
     */
    public int getChannel() {
        return mChannel;
    }


    /*
    Save point in firebase
     */
    public void savePoint(TPoint tp) {
        if (tp != null && mGeoFire != null) {
            // assign new firebase id
            tp.setFirebaseID(mDatabaseRootRef.push().getKey());
            // set geofire location
            mGeoFire.setLocation(tp.getFirebaseID(), tp.getGeoLocation());
            mDatabaseRootRef.child(tp.getFirebaseID()).child("data").child("info").setValue(tp);
            if (mFirebaseResponse != null) {
                mFirebaseResponse.onPointTagged(true);
            }
        }
    }


    /*
    Remove point from firebase
     */
    public void removePoint(TPoint tp) {
        mDatabaseRootRef.child(tp.getFirebaseID()).removeValue();
        if (mFirebaseResponse != null) {
            mFirebaseResponse.onPointRemoved(tp);
        }
    }


    /*
    Download all points nearby a center point
     */
    public void getPointsNearby(final TPoint tpCenter) {
        // download all points in a 15KM radius
        Log.v("TEST", "started 15Km radius search for tpoints near => lat: " + tpCenter.getLat() + " lon: " + tpCenter.getLon());
        mGeoFire = new GeoFire(mDatabaseRootRef);
        mGeoQuery = mGeoFire.queryAtLocation(tpCenter.getGeoLocation(), 15);
        mGeoListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                mDatabaseRootRef.child(key).child("data").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.v("TEST", "onChildAdded()");
                        TPoint tp = dataSnapshot.getValue(TPoint.class);
                        if (tp != null) {
                            tp.setFirebaseID(key);
                            tp.setLon(location.longitude);
                            tp.setLat(location.latitude);
                            tp.setDistance(Tools.calculateDistance(tpCenter, tp));
                            if (mFirebaseResponse != null) {
                                mFirebaseResponse.onPointFound(tp);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.v("TEST", "onChildChanged()");
                        TPoint tp = dataSnapshot.getValue(TPoint.class);
                        if (tp != null) {
                            tp.setFirebaseID(key);
                            tp.setLon(location.longitude);
                            tp.setLat(location.latitude);
                            if (mFirebaseResponse != null) {
                                mFirebaseResponse.onPointRemoved(tp);
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        };
        mGeoQuery.addGeoQueryEventListener(mGeoListener);
    }


    /*
    update center point
     */
    public boolean setCenterPoint(TPoint center) {
        if (center == null) return false;

        // if this is the first point of user
        if (mCurrentCenter == null) {
            mCurrentCenter = center;
            mCurrentCenter.setFirebaseID("center");
            return true;
        }

        // if user is more than 4KM away from its previous center point
        int dist = Tools.calculateDistance(center, mCurrentCenter);
        if (dist > 50 && dist < 4000) {
            mCurrentCenter = center;
            mCurrentCenter.setFirebaseID("center");
            return true;
        }

        return false;
    }


    /*
    Release memory refs
     */
    public void destroy() {
        mDatabaseRootRef = null;
        mGeoFire = null;
        mGeoQuery = null;
        mGeoListener = null;
        mFirebaseResponse = null;
        mCurrentCenter = null;
    }
}