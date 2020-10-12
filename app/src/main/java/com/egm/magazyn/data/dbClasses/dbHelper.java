package com.egm.magazyn.data.dbClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.egm.magazyn.data.dbClasses.dbContract.*;

public class dbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG= dbHelper.class.getSimpleName();

    private static final String DB_NAME="warehouse.db";
    private static final int DB_VERSION=1;

    public dbHelper(Context context) {super(context, DB_NAME, null, DB_VERSION);}


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String REMINDERS_CREATE_TABLE = "CREATE TABLE " + remindersEntry.TABLE_NAME + " ("
                + remindersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + remindersEntry.COLUMN_EQUIPMENT_NAME + " TEXT NOT NULL, "
                + remindersEntry.COLUMN_SERIAL_NUMBER + " TEXT NOT NULL, "
                + remindersEntry.COLUMN_NEXT_INSPECTION_DATE + " TEXT NOT NULL);";

        String WAREHOUSE_CREATE_TABLE = "CREATE TABLE " + warehouseEntry.TABLE_NAME + " ( "
                + warehouseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + warehouseEntry.COL_PRODUCT_NAME + " TEXT NOT NULL, "
                + warehouseEntry.COL_QUANTITY + " REAL NOT NULL, "
                + warehouseEntry.COL_ACCOUNTING_UNIT + " INTEGER NOT NULL, "
                + warehouseEntry.COL_UNIT_PRICE + " REAL NOT NULL, "
                + warehouseEntry.COL_QUANTITY_AFTER_LAST_DELIVERY + " REAL NOT NULL, "
                + warehouseEntry.COL_LOW_QUANTITY_WARNING + " REAL, "
                + warehouseEntry.COL_LOW_QUANTITY_WARNING_UNIT + " INTEGER, "
                + warehouseEntry.COL_LOW_QUANTITY_ALARM + " REAL, "
                + warehouseEntry.COL_LOW_QUANTITY_ALARM_UNIT + " INTEGER, "
                + warehouseEntry.COL_SOURCE + " INTEGER NOT NULL, "
                + warehouseEntry.COL_LAST_DELIVERY_PRICE + " REAL, "
                + warehouseEntry.COL_LAST_DELIVERY_DATE + " TEXT);";

        sqLiteDatabase.execSQL(REMINDERS_CREATE_TABLE);
        sqLiteDatabase.execSQL(WAREHOUSE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
