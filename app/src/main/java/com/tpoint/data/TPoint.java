package com.tpoint.data;

import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xnorcode on 18/02/2018.
 */

@IgnoreExtraProperties
public class TPoint {

    private long sqlID;
    private String firebaseID;
    private double lat;
    private double lon;
    private int distance;
    private long creationDate;
    private long archivedDate;


    public TPoint() {

    }


    /*
    Firebase Methods
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lat", this.lat);
        result.put("lon", this.lon);
        result.put("distance", this.distance);
        result.put("creationDate", this.creationDate);
        result.put("archiveDate", this.archivedDate);
        return result;
    }


    /*
    Create Geolocation
     */
    @Exclude
    public GeoLocation getGeoLocation() {
        return new GeoLocation(this.lat, this.lon);
    }


    /*
    Setters & Getters
     */
    @Exclude
    public long getSqlID() {
        return sqlID;
    }

    @Exclude
    public void setSqlID(long sqlID) {
        this.sqlID = sqlID;
    }

    @Exclude
    public String getFirebaseID() {
        return firebaseID;
    }

    @Exclude
    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    @Exclude
    public double getLat() {
        return lat;
    }

    @Exclude
    public void setLat(double lat) {
        this.lat = lat;
    }

    @Exclude
    public double getLon() {
        return lon;
    }

    @Exclude
    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getCreationDate() {
        return creationDate;
    }

    @Exclude
    public void setCreationDate() {
        this.creationDate = new GregorianCalendar().getTimeInMillis();
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(long archivedDate) {
        this.archivedDate = archivedDate;
    }

    @Exclude
    public int getDistance() {
        return distance;
    }

    @Exclude
    public void setDistance(int distance) {
        this.distance = distance;
    }
}