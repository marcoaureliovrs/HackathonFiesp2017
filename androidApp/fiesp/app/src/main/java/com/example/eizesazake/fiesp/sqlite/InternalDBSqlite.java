package com.example.eizesazake.fiesp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InternalDBSqlite extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db";

    // Contacts table name
    private static final String TABLE_NAME = "table_db";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATLONG = "latlong";
    private static final String KEY_ALARM = "alarm";
    private static final String KEY_ADDRESS = "address";

    public InternalDBSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                KEY_LATLONG + " TEXT," +
                KEY_ALARM + " TEXT," +
                KEY_ADDRESS + " TEXT" +
                ")";
        db.execSQL(CREATE_USER_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /*
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addData(String latlong) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATLONG, latlong);

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        // Closing database connection
        db.close();
    }

    // Getting single contact
    public String getLatlong() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                // String table
                TABLE_NAME,
                // String[] columns
                new String[]{
                        KEY_LATLONG,
                        KEY_ALARM,
                        KEY_ADDRESS
                },
                // String selection
                null,
                // String[] selectionArgs
                null,
                // String groupBy
                null,
                // String having
                null,
                // String orderBy
                null,
                // String limit
                null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor.getString(0);
    }

    // Updating single userLogin
    public int updateUserLogin(String latlong) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATLONG, latlong);

        // updating row
        return db.update(
                // String table
                TABLE_NAME,
                // ContentValues values
                values,
                // String whereClause
                null,
                // String[] whereArgs
                null);
    }

    // Deleting single contact
    public void deleteLatlong() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                // String table
                TABLE_NAME,
                // String whereClause
                null,
                // String[] whereArgs
                null);
        db.close();
    }


    // Getting contacts Count
    public int getLatlongCount(InternalDBSqlite loginSqlite) {
        Cursor cursor = null;

        try {
            String countQuery = "SELECT  * FROM " + TABLE_NAME;
            SQLiteDatabase db = loginSqlite.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            // return count
            return cursor.getCount();
        } finally {
            cursor.close();
        }
    }

}

