package com.tpoint.io.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.tpoint.data.TPoint;
import com.tpoint.data.db.DataContract;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class SQLiteUtils {


    /*
    Build TPoint from database cursor
     */
    public static TPoint buildTpoint(Cursor cursor) {
        if (cursor == null) return null;

        TPoint tp = new TPoint();
        tp.setSqlID(cursor.getLong(DataContract.TpointEntry.INDEX_COL_TPOINT_ID));
        tp.setFirebaseID(cursor.getString(DataContract.TpointEntry.INDEX_COL_FIREBASE_ID));
        tp.setLat(cursor.getDouble(DataContract.TpointEntry.INDEX_COL_LAT));
        tp.setLon(cursor.getDouble(DataContract.TpointEntry.INDEX_COL_LON));
        tp.setDistance(cursor.getInt(DataContract.TpointEntry.INDEX_COL_DISTANCE));
        tp.setCreationDate(cursor.getLong(DataContract.TpointEntry.INDEX_COL_CREATION_DATE));
        tp.setArchivedDate(cursor.getLong(DataContract.TpointEntry.INDEX_COL_ARCHIVE_DATE));
        return tp;
    }


    /*
    Build Content Values from TPoint
     */
    public static ContentValues buildContentValues(TPoint tp) {
        if (tp == null) return null;

        ContentValues tpValues = new ContentValues();
        tpValues.put(DataContract.TpointEntry.COLUMN_FIREBASEID, tp.getFirebaseID());
        tpValues.put(DataContract.TpointEntry.COLUMN_LAT, tp.getLat());
        tpValues.put(DataContract.TpointEntry.COLUMN_LON, tp.getLon());
        tpValues.put(DataContract.TpointEntry.COLUMN_DISTANCE, tp.getDistance());
        tpValues.put(DataContract.TpointEntry.COLUMN_CREATION_DATE, tp.getCreationDate());
        tpValues.put(DataContract.TpointEntry.COLUMN_ARCHIVE_DATE, tp.getArchivedDate());
        return tpValues;
    }
}
