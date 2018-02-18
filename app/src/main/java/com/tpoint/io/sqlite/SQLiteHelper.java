package com.tpoint.io.sqlite;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tpoint.data.TPoint;
import com.tpoint.data.db.DataContract;
import com.tpoint.utils.Tools;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class SQLiteHelper {


    /*
    Save location in SQL.
     */
    public static boolean savePoint(Context context, TPoint tp) {
        if (tp != null) {

            long tpId;
            ContentValues tpValues = SQLiteUtils.buildContentValues(tp);

            Cursor cursor = context.getContentResolver().query(DataContract.TpointEntry.CONTENT_URI,
                    new String[]{DataContract.TpointEntry._ID},
                    DataContract.TpointEntry._ID + " = ?",
                    new String[]{String.valueOf(tp.getSqlID())},
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                tpId = cursor.getLong(DataContract.TpointEntry.INDEX_COL_TPOINT_ID);
                context.getContentResolver().update(DataContract.TpointEntry.CONTENT_URI, tpValues, "_id=" + tpId, null);
                cursor.close();
                Log.v("SQLite", "TPoint Updated in SQLite!");
                return true;
            } else {
                Uri insertedUri = context.getContentResolver().insert(DataContract.TpointEntry.CONTENT_URI, tpValues);
                tpId = ContentUris.parseId(insertedUri);
                Log.v("SQLite", "Saving New TPoint in SQLite!");
                return true;
            }

        }

        return false;
    }


    /*
    Save all location points in SQLite
     */
    public static boolean savePoint(Context context, ArrayList<TPoint> points) {
        if (points != null) {
            Vector<ContentValues> cvVector = new Vector<ContentValues>(points.size());

            for (int i = 0; i < points.size(); i++) {
                TPoint tp = points.get(i);
                ContentValues tpValues = SQLiteUtils.buildContentValues(tp);

                cvVector.add(tpValues);
            }

            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);

            context.getContentResolver().bulkInsert(DataContract.TpointEntry.CONTENT_URI, cvArray);
            Log.v("SQLite", "ALL TPoints SAVED in SQLite!");

            return true;
        }

        return false;
    }


    /*
    Get specific Point
     */
    public static TPoint getPoint(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(DataContract.TpointEntry.CONTENT_URI,
                null,
                DataContract.TpointEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            TPoint tp = SQLiteUtils.buildTpoint(cursor);
            Log.v("SQLite", "TPoint downloaded from SQLite!");
            cursor.close();
            return tp;
        }

        return null;
    }


    /*
    Update point.
     */
    public static boolean updatePoint(Context context, TPoint tp) {
        if (tp != null) {
            long tpId;

            Cursor cursor = context.getContentResolver().query(DataContract.TpointEntry.CONTENT_URI,
                    null,
                    DataContract.TpointEntry._ID + " = ?",
                    new String[]{String.valueOf(tp.getSqlID())},
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                tpId = cursor.getLong(DataContract.TpointEntry.INDEX_COL_TPOINT_ID);
                ContentValues tpValues = SQLiteUtils.buildContentValues(tp);
                context.getContentResolver().update(DataContract.TpointEntry.CONTENT_URI, tpValues, "_id=" + tpId, null);
                cursor.close();
                Log.v("SQLite", "TPoint UPDATED in SQLite!");

                return true;
            }
        }

        return false;
    }


    /*
    Check and update all points distance.
    Return TPoint if is in a 500m range.
     */
    public static TPoint checkPointsDistance(Context context, TPoint current) {
        TPoint nearbyTP = null;

        Cursor cursor = context.getContentResolver().query(DataContract.TpointEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            long tpId;
            while (cursor.moveToNext()) {
                tpId = cursor.getLong(DataContract.TpointEntry.INDEX_COL_TPOINT_ID);
                TPoint tp = SQLiteUtils.buildTpoint(cursor);

                tp.setDistance(Tools.calculateDistance(tp, current));

                // save point if distance less than 500m
                if (tp.getDistance() < 500) {
                    if (nearbyTP == null) nearbyTP = tp;
                        // get closest point
                    else if (tp.getDistance() < nearbyTP.getDistance()) nearbyTP = tp;
                }

                // update SQLite point data entry
                ContentValues tpValues = SQLiteUtils.buildContentValues(tp);
                context.getContentResolver().update(DataContract.TpointEntry.CONTENT_URI, tpValues, "_id=" + tpId, null);
            }
            cursor.close();
            Log.v("SQLite", "TPoints Distance UPDATED in SQLite!");
        }

        return nearbyTP;
    }


    /*
    Delete point
     */
    public static boolean deletePoint(Context context, String firebaseID) {
        try {
            context.getContentResolver().delete(DataContract.TpointEntry.CONTENT_URI, DataContract.TpointEntry.COLUMN_FIREBASEID + " = ?", new String[]{String.valueOf(firebaseID)});
            Log.v("SQLite", "TPoint: " + firebaseID + " DELETED from SQLite!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /*
    Delete all points
    */
    public static boolean deletePoint(Context context, boolean all) {
        if (all) {
            try {
                context.getContentResolver().delete(DataContract.TpointEntry.CONTENT_URI, null, null);
                Log.v("SQLite", "All TPoints DELETED from SQLite!");
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
