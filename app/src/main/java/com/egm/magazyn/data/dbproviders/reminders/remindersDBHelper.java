package com.egm.magazyn.data.dbproviders.reminders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry;

public class remindersDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG=remindersDBHelper.class.getSimpleName();

    private static final String DB_NAME="reminders.db";
    private static final int DB_VERSION=1;

    public remindersDBHelper(Context context) {super(context, DB_NAME, null, DB_VERSION);}


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + remindersEntry.TABLE_NAME + " ("
                + remindersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + remindersEntry.COLUMN_EQUIPMENT_NAME + " TEXT NOT NULL, "
                + remindersEntry.COLUMN_NEXT_INSPECTION_DATE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
