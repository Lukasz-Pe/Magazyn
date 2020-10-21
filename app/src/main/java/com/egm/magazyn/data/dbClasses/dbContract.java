package com.egm.magazyn.data.dbClasses;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class dbContract {
    public static final String CONTENT_AUTHORITY="com.egm.magazyn";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    private dbContract(){}

    public static final class remindersEntry implements BaseColumns{
        public final static String TABLE_NAME="reminders";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COL_EQUIPMENT_NAME ="name";
        public final static String COL_NEXT_INSPECTION_DATE ="next_inspection_date";
        public final static String COL_SERIAL_NUMBER ="serial_number";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;
    }

    public static final class warehouseEntry implements BaseColumns{
        public final static String TABLE_NAME="products";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COL_PRODUCT_NAME="name";
        public final static String COL_QUANTITY="quantity";
        public final static String COL_QUANTITY_AFTER_LAST_DELIVERY ="quantity_after_last_delivery";
        public final static String COL_UNIT_PRICE="unit_price";
        public final static String COL_ACCOUNTING_UNIT="unit_of_account";
        public final static String COL_LOW_QUANTITY_WARNING="low_quantity_warning";
        public final static String COL_LOW_QUANTITY_WARNING_UNIT="low_quantity_warning_unit";
        public final static String COL_LOW_QUANTITY_ALARM="low_quantity_alarm";
        public final static String COL_LOW_QUANTITY_ALARM_UNIT="low_quantity_alarm_unit";
        public final static String COL_SOURCE="source";
        public final static String COL_LAST_DELIVERY_PRICE="last_delivery_price";
        public final static String COL_LAST_DELIVERY_DATE="last_delivery_date";

        //Possible values of spinners
        //Overall
        public static final int OPTION_UNDEFINED = 0;

        //Unit price
        public static final int UNIT_PRICE_EUR_KG = 1;
        public static final int UNIT_PRICE_EUR_PIECE =2;

        public static boolean validUnitPrice(int value){
            if(value==OPTION_UNDEFINED||value==UNIT_PRICE_EUR_KG||value==UNIT_PRICE_EUR_PIECE){
                return true;
            }
            return false;
        }

        //Low quantity Warnings/alerts:
        public static final int LOW_QUANTITY_PERCENT=1;
        public static final int LOW_QUANTITY_UNIT=2;

        public static boolean validLowQuantity(int value){
            if(value==OPTION_UNDEFINED||value==LOW_QUANTITY_PERCENT||value==LOW_QUANTITY_UNIT){
                return true;
            }
            return false;
        }

        //Source:
        public static final int SOURCE_INTERNAL=1;
        public static final int SOURCE_EXTERNAL=2;

        public static boolean validSource(int value){
            if(value==OPTION_UNDEFINED||value==SOURCE_INTERNAL||value==SOURCE_EXTERNAL){
                return true;
            }
            return false;
        }

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ warehouseEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+ warehouseEntry.TABLE_NAME;
    }

    public static final class customersEntry implements BaseColumns{
        public final static String TABLE_NAME = "customers";
        public final static Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COL_NAMES = "names";
        public final static String COL_SURNAME = "surname";
        public final static String COL_PHONE_NUMBER = "phone_number";
        public final static String COL_ADRESS = "adress";
        public final static String COL_NOTES = "notes";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+remindersEntry.TABLE_NAME;
    }

    public static final class deliveryEntry implements BaseColumns{
        public final static String TABLE_NAME = "deliveries";
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        public final static String _ID=BaseColumns._ID;
        public final static String COL_CLIENT_ID = "customer_id";
        public final static String COL_PRODUCTS_IDS = "products_ids";
        public final static String COL_ALL_PRODUCTS_LOADED = "all_products_are_loaded";
        public final static String COL_DELIVERY_STATE = "is_delivered";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+deliveryEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+deliveryEntry.TABLE_NAME;
    }

}
