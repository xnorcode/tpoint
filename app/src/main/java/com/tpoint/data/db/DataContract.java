package com.tpoint.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.tpoint";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TPOINT = "tpoint";

    public static final class TpointEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TPOINT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TPOINT;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TPOINT;

        public static final Uri buildTpointUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "tpoints";
        public static final String COLUMN_FIREBASEID = "firebase_id";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_ARCHIVE_DATE = "archive_date";

        public static final int INDEX_COL_TPOINT_ID = 0;
        public static final int INDEX_COL_FIREBASE_ID = 1;
        public static final int INDEX_COL_LAT = 2;
        public static final int INDEX_COL_LON = 3;
        public static final int INDEX_COL_DISTANCE = 4;
        public static final int INDEX_COL_CREATION_DATE = 5;
        public static final int INDEX_COL_ARCHIVE_DATE = 6;
    }
}