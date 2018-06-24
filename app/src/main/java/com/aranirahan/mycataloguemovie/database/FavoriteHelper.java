package com.aranirahan.mycataloguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static android.provider.BaseColumns._ID;

public class FavoriteHelper {

    private static String TABLE_NAME = FavoriteTable.TABLE_NAME;

    private Context context;
    private DbHelper dbHelper;

    private SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_NAME,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public Cursor queryProvider() {
        return database.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC"
        );
    }

    public long insertProvider(ContentValues values) {
        return database.insert(TABLE_NAME, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(TABLE_NAME, values, _ID + " = ?", new String[]{id}
        );
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_NAME, _ID + " = ?", new String[]{id}
        );
    }
}
