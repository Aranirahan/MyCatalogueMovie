package com.aranirahan.mycataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static int DB_VERSION = 1;
    public static String DB_NAME = "my_catalogue_movie";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_MOVIE = "CREATE TABLE " + FavoriteTable.TABLE_NAME + " (" +
                FavoriteTable.ID_ + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteTable.POSTER + " TEXT, " +
                FavoriteTable.BACKDROP + " TEXT, " +
                FavoriteTable.TITLE + " TEXT, " +
                FavoriteTable.OVERVIEW + " TEXT, " +
                FavoriteTable.RELEASE_DATE + " TEXT, " +
                FavoriteTable.VOTE + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + FavoriteTable.TABLE_NAME);
        onCreate(db);
    }
}
