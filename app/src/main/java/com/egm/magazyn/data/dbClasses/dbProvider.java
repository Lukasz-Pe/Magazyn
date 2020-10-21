package com.egm.magazyn.data.dbClasses;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class dbProvider extends ContentProvider {

    private static final String LOG_TAG= dbProvider.class.getSimpleName();
    private dbHelper dbHelper;
    //IDs for UriMatcher
    private static final int
            REMINDERS_TABLE =100, REMINDER_ITEM =101,
            WAREHOUSE_TABLE=102, WAREHOUSE_ITEM=103,
            CLIENTS_TABLE=104, CLIENTS_ITEM=105;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Database and provider helper objects
    static{
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.remindersEntry.TABLE_NAME, REMINDERS_TABLE);
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.remindersEntry.TABLE_NAME+"/#", REMINDER_ITEM);
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.warehouseEntry.TABLE_NAME, WAREHOUSE_TABLE);
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.warehouseEntry.TABLE_NAME+"/#", WAREHOUSE_ITEM);
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.customersEntry.TABLE_NAME, CLIENTS_TABLE);
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.customersEntry.TABLE_NAME+"/#", CLIENTS_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper =new dbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db= dbHelper.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        Log.i("uri",uri.toString());
        Log.i("URIMatcher", String.valueOf(match));
        switch(match){
            case REMINDERS_TABLE:{
                cursor=db.query(dbContract.remindersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case REMINDER_ITEM:{
                selection= dbContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(dbContract.remindersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case WAREHOUSE_TABLE:{
                cursor=db.query(dbContract.warehouseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case WAREHOUSE_ITEM:{
                selection= dbContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(dbContract.warehouseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case CLIENTS_TABLE:{
                cursor=db.query(dbContract.customersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case CLIENTS_ITEM:{
                selection= dbContract.customersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(dbContract.customersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            default:{
                throw new IllegalArgumentException("Unable to query unknown URI: " + uri);
            }
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch(match){
            case REMINDERS_TABLE:{
                return dbContract.remindersEntry.CONTENT_LIST_TYPE;
            }
            case REMINDER_ITEM:{
                return dbContract.remindersEntry.CONTENT_ITEM_TYPE;
            }
            case WAREHOUSE_TABLE:{
                return dbContract.warehouseEntry.CONTENT_LIST_TYPE;
            }
            case WAREHOUSE_ITEM:{
                return dbContract.warehouseEntry.CONTENT_ITEM_TYPE;
            }
            case CLIENTS_TABLE:{
                return dbContract.customersEntry.CONTENT_LIST_TYPE;
            }
            case CLIENTS_ITEM:{
                return dbContract.customersEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch(sUriMatcher.match(uri)){
            case REMINDERS_TABLE:{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String name = contentValues.getAsString(dbContract.remindersEntry.COL_EQUIPMENT_NAME);
                String date = contentValues.getAsString(dbContract.remindersEntry.COL_NEXT_INSPECTION_DATE);
                if (name == null) {
                    throw new IllegalArgumentException("Name is required field!");
                }
                if(date==null){
                    throw new IllegalArgumentException("Next inspection date is required field!");
                }
                getContext().getContentResolver().notifyChange(uri, null);
                long id = db.insert(dbContract.remindersEntry.TABLE_NAME, null, contentValues);
                if(id<0){
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            }
            case WAREHOUSE_TABLE:{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String name = contentValues.getAsString(dbContract.warehouseEntry.COL_PRODUCT_NAME);
                double quantity = contentValues.getAsDouble(dbContract.warehouseEntry.COL_QUANTITY);
                int accounting_unit = contentValues.getAsInteger(dbContract.warehouseEntry.COL_ACCOUNTING_UNIT);
                double unit_price = contentValues.getAsDouble(dbContract.warehouseEntry.COL_UNIT_PRICE);
                double quantity_after_last_delivery = 14.5;//contentValues.getAsDouble(dbContract.warehouseEntry.COL_QUANTITY_AFTER_LAST_DELIVERY);
                if (name == null) {
                    throw new IllegalArgumentException("Name is required field!");
                }
                if(quantity<0||unit_price<0){
                    throw new IllegalArgumentException("Parameter has to be >0!");
                }
                if(!dbContract.warehouseEntry.validUnitPrice(accounting_unit)){
                    Log.w("accounting_unit",String.valueOf(accounting_unit));
                    throw new IllegalArgumentException("Illegal value!");
                }
//                if(quantity_after_last_delivery>quantity){
//                    throw new IllegalArgumentException("Parameter has to be greater than available quantity!");
//                }
                getContext().getContentResolver().notifyChange(uri, null);
                long id = db.insert(dbContract.warehouseEntry.TABLE_NAME, null, contentValues);
                if(id<0){
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            }
            case CLIENTS_TABLE:{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String names = contentValues.getAsString(dbContract.customersEntry.COL_NAMES);
                String surname = contentValues.getAsString(dbContract.customersEntry.COL_SURNAME);
                String phoneNumber = contentValues.getAsString(dbContract.customersEntry.COL_PHONE_NUMBER);
                String adress = contentValues.getAsString(dbContract.customersEntry.COL_ADRESS);
                if (names == null) {
                    throw new IllegalArgumentException("Names is required field!");
                }
                if(surname==null){
                    throw new IllegalArgumentException("Surnames is required field!");
                }
                if(phoneNumber==null){
                    throw new IllegalArgumentException("Phone number is required field!");
                }
                if(adress==null){
                    throw new IllegalArgumentException("Adress is required field!");
                }
                getContext().getContentResolver().notifyChange(uri, null);
                long id = db.insert(dbContract.customersEntry.TABLE_NAME, null, contentValues);
                if(id<0){
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);
            }
            default:{
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)){
            case REMINDERS_TABLE:{
                rowsDeleted=db.delete(dbContract.remindersEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case REMINDER_ITEM:{
                selection= dbContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(dbContract.remindersEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case WAREHOUSE_TABLE:{
                rowsDeleted=db.delete(dbContract.warehouseEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case WAREHOUSE_ITEM:{
                selection= dbContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(dbContract.warehouseEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case CLIENTS_TABLE:{
                rowsDeleted=db.delete(dbContract.customersEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case CLIENTS_ITEM:{
                selection= dbContract.customersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(dbContract.customersEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:{
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
            }
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if(contentValues.size()==0){
            return 0;
        }
        switch(sUriMatcher.match((uri))){
            case REMINDERS_TABLE:{
                return updateReminder(uri,contentValues, selection, selectionArgs);
            }
            case REMINDER_ITEM:{
                selection= dbContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateReminder(uri,contentValues, selection, selectionArgs);
            }
            case WAREHOUSE_TABLE:{
                return updateWarehouse(uri,contentValues, selection, selectionArgs);
            }
            case WAREHOUSE_ITEM:{
                selection= dbContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateWarehouse(uri,contentValues, selection, selectionArgs);
            }
            case CLIENTS_TABLE:{
                return updateClients(uri,contentValues, selection, selectionArgs);
            }
            case CLIENTS_ITEM:{
                selection= dbContract.customersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateClients(uri,contentValues, selection, selectionArgs);
            }
            default:{
                throw new IllegalArgumentException("Unable to update unknown URI: " + uri);
            }
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(dbContract.remindersEntry.COL_EQUIPMENT_NAME)){
            String name = values.getAsString(dbContract.remindersEntry.COL_EQUIPMENT_NAME);
            if(name==null){
                throw new IllegalArgumentException("Name is required field!");
            }
        }
        if(values.containsKey(dbContract.remindersEntry.COL_NEXT_INSPECTION_DATE)){
            String date = values.getAsString(dbContract.remindersEntry.COL_NEXT_INSPECTION_DATE);
            if(date==null){
                throw new IllegalArgumentException("Next inspection date is required field!");
            }
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return db.update(dbContract.remindersEntry.TABLE_NAME,values,selection,selectionArgs);
    }

    private int updateWarehouse(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(dbContract.warehouseEntry.COL_PRODUCT_NAME)){
            String name = values.getAsString(dbContract.warehouseEntry.COL_PRODUCT_NAME);
            if(name==null){
                throw new IllegalArgumentException("Name is required field!");
            }
        }
        if(values.containsKey(dbContract.warehouseEntry.COL_QUANTITY)){
            double quantity = values.getAsDouble(dbContract.warehouseEntry.COL_QUANTITY);
            if(quantity<0){
                throw new IllegalArgumentException("Value <0!");
            }
        }
        if(values.containsKey(dbContract.warehouseEntry.COL_ACCOUNTING_UNIT)){
            int accounting_unit = values.getAsInteger(dbContract.warehouseEntry.COL_ACCOUNTING_UNIT);
            if(accounting_unit<0){
                throw new IllegalArgumentException("Value <0!");
            }
        }
        if(values.containsKey(dbContract.warehouseEntry.COL_UNIT_PRICE)){
            double unit_price = values.getAsDouble(dbContract.warehouseEntry.COL_UNIT_PRICE);
            if(unit_price<0){
                throw new IllegalArgumentException("Value <0!");
            }
        }
        if(values.containsKey(dbContract.warehouseEntry.COL_QUANTITY_AFTER_LAST_DELIVERY)){
            double quant_after_del = values.getAsDouble(dbContract.warehouseEntry.COL_QUANTITY_AFTER_LAST_DELIVERY);
            double quantity = values.getAsDouble(dbContract.warehouseEntry.COL_QUANTITY);
            if(quant_after_del<quantity){
                throw new IllegalArgumentException("COL_QUANTITY_AFTER_LAST_DELIVERY < COL_QUANTITY!");
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return db.update(dbContract.warehouseEntry.TABLE_NAME,values,selection,selectionArgs);
    }

    private int updateClients(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(dbContract.customersEntry.COL_NAMES)){
            String value = values.getAsString(dbContract.customersEntry.COL_NAMES);
            if(value==null){
                throw new IllegalArgumentException("Name is required field!");
            }
        }
        if(values.containsKey(dbContract.customersEntry.COL_SURNAME)){
            String value = values.getAsString(dbContract.customersEntry.COL_SURNAME);
            if(value==null){
                throw new IllegalArgumentException("Surname is required field!");
            }
        }
        if(values.containsKey(dbContract.customersEntry.COL_PHONE_NUMBER)){
            String value = values.getAsString(dbContract.customersEntry.COL_PHONE_NUMBER);
            if(value==null){
                throw new IllegalArgumentException("Phone number is required field!");
            }
        }
        if(values.containsKey(dbContract.customersEntry.COL_ADRESS)){
            String value = values.getAsString(dbContract.customersEntry.COL_ADRESS);
            if(value==null){
                throw new IllegalArgumentException("Adress is required field!");
            }
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return db.update(dbContract.customersEntry.TABLE_NAME,values,selection,selectionArgs);
    }

}
