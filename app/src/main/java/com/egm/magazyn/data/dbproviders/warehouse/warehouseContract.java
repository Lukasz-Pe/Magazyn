package com.egm.magazyn.data.dbproviders.warehouse;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class warehouseContract {
    public static final String CONTENT_AUTHORITY="com.egm.magazyn";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    private warehouseContract(){}

    public static final class warehouseEntry implements BaseColumns{
        public final static String TABLE_NAME="warehouse";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COL_PRODUCT_NAME="name";
        public final static String COL_QUANTITY="quantity";
        public final static String COL_LAST_DELIVERY_QUANTITY="last_delivery_quantity";
        public final static String COL_UNIT="unit";
        public final static String COL_UNIT_PRICE="unit_price";
        public final static String COL_ACCOUNTING_UNIT="unit_of_account";
        public final static String COL_LOW_QUANTITY_WARNING="low_quantity_warning";
        public final static String COL_LOW_QUANTITY_WARNING_UNIT="low_quantity_warning_unit";
        public final static String COL_LOW_QUANTITY_ALARM="low_quantity_alarm";
        public final static String COL_LOW_QUANTITY_ALARM_UNIT="low_quantity_alarm_unit";
        public final static String COL_SOURCE="source";
        public final static String COL_LAST_DELIVERY_PRICE="last_delivery_price";
        public final static String COL_LAST_DELIVERY_DATE="last_delivery_date";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ warehouseEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ warehouseEntry.TABLE_NAME;
    }
}
