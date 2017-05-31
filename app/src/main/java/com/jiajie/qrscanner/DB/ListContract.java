package com.jiajie.qrscanner.DB;

import android.provider.BaseColumns;

public class ListContract {
    public static final String DB_NAME = "com.jiajie.qrsscanner.db";
    public static final int DB_VERSION = 3;

    public class ScannedEntry implements BaseColumns{
        public static final String TABLE = "tasks";
        public static final String COL_RESULT = "title";
        public static final String LAT = "latitude";
        public static final String LNG = "longitude";
    }
}
