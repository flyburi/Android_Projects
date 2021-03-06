
package com.uyuni.fastlearner.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BaseTable {

    // Database table
    public static final String TABLE_BASE = "base_table";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_CATEGORY = "category";

    public static final String COLUMN_WORD = "word";

    public static final String COLUMN_DEFINITION = "definition";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table " + TABLE_BASE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CATEGORY + " text not null, "
            + COLUMN_WORD + " text not null," + COLUMN_DEFINITION + " text not null" + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(BaseTable.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BASE);
        onCreate(database);
    }
}
