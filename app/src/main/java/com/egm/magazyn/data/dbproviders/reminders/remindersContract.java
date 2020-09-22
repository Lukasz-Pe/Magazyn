package com.egm.magazyn.data.dbproviders.reminders;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class remindersContract {
    public static final String CONTENT_AUTHORITY="com.egm.magazyn";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    private remindersContract(){}

    public static final class remindersEntry implements BaseColumns{
        public final static String TABLE_NAME="reminders";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_EQUIPMENT_NAME="name";
        public final static String COLUMN_NEXT_INSPECTION_DATE="next_inspection_date";
        public final static String COLUMN_SERIAL_NUMBER="serial_number";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;
    }
}
