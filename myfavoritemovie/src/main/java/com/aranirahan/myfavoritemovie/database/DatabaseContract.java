package com.aranirahan.myfavoritemovie.database;

import android.database.Cursor;
import android.net.Uri;

import com.aranirahan.myfavoritemovie.database.FavoriteTable;

public class DatabaseContract {

    public static final String AUTHORITY = "com.aranirahan.mycataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(FavoriteTable.TABLE_NAME)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

}
