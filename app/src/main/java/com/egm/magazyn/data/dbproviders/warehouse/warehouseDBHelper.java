package com.egm.magazyn.data.dbproviders.warehouse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egm.magazyn.data.dbproviders.warehouse.warehouseContract.warehouseEntry;

public class warehouseDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG= warehouseDBHelper.class.getSimpleName();

    private static final String DB_NAME="warehouse.db";
    private static final int DB_VERSION=1;

    public warehouseDBHelper(Context context) {super(context, DB_NAME, null, DB_VERSION);}


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + warehouseEntry.TABLE_NAME + " ("
                + warehouseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + warehouseEntry.COL_PRODUCT_NAME + "TEXT NOT NULL, "
                + warehouseEntry.COL_QUANTITY + "REAL NOT NULL, "
                + warehouseEntry.COL_ACCOUNTING_UNIT + "INTEGER NOT NULL, "
                + warehouseEntry.COL_UNIT_PRICE + "REAL NOT NULL, "
                + warehouseEntry.COL_UNIT + "INTEGER NOT NULL, "
                + warehouseEntry.COL_LOW_QUANTITY_WARNING + "REAL, "
                + warehouseEntry.COL_LOW_QUANTITY_WARNING_UNIT + "INTEGER NOT NULL, "
                + warehouseEntry.COL_LOW_QUANTITY_ALARM + "REAL, "
                + warehouseEntry.COL_LOW_QUANTITY_ALARM_UNIT + "INTEGER NOT NULL, "
                + warehouseEntry.COL_SOURCE + "INTEGER NOT NULL, "
                + warehouseEntry.COL_LAST_DELIVERY_PRICE + "REAL, "
                + warehouseEntry.COL_LAST_DELIVERY_DATE + "TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
