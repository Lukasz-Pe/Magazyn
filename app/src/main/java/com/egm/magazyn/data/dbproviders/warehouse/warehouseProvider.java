package com.egm.magazyn.data.dbproviders.warehouse;

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

public class warehouseProvider extends ContentProvider {

    private static final String LOG_TAG= warehouseProvider.class.getSimpleName();
    private warehouseDBHelper warehousedb;
    //IDs for UriMatcher
    private static final int WAREHOUSE_TABLE =100, WAREHOUSE_ITEM =101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Database and provider helper objects
    static{
        sUriMatcher.addURI(warehouseContract.CONTENT_AUTHORITY, warehouseContract.warehouseEntry.TABLE_NAME, WAREHOUSE_TABLE);
        sUriMatcher.addURI(warehouseContract.CONTENT_AUTHORITY, warehouseContract.warehouseEntry.TABLE_NAME+"/#", WAREHOUSE_ITEM);
    }

    @Override
    public boolean onCreate() {
        warehousedb =new warehouseDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db= warehousedb.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        Log.i("uri",uri.toString());
        Log.i("URIMatcher", String.valueOf(match));
        switch(match){
            case WAREHOUSE_TABLE:{
                cursor=db.query(warehouseContract.warehouseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case WAREHOUSE_ITEM:{
                selection= warehouseContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(warehouseContract.warehouseEntry.TABLE_NAME,
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
            case WAREHOUSE_TABLE:{
                return warehouseContract.warehouseEntry.CONTENT_LIST_TYPE;
            }
            case WAREHOUSE_ITEM:{
                return warehouseContract.warehouseEntry.CONTENT_ITEM_TYPE;
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
            case WAREHOUSE_TABLE:{
                SQLiteDatabase db = warehousedb.getReadableDatabase();
                String name = contentValues.getAsString(warehouseContract.warehouseEntry.COLUMN_EQUIPMENT_NAME);
                String date = contentValues.getAsString(warehouseContract.warehouseEntry.COLUMN_NEXT_INSPECTION_DATE);
                if (name == null) {
                    throw new IllegalArgumentException("Name is required field!");
                }
                if(date==null){
                    throw new IllegalArgumentException("Next inspection date is required field!");
                }
                getContext().getContentResolver().notifyChange(uri, null);
                long id = db.insert(warehouseContract.warehouseEntry.TABLE_NAME, null, contentValues);
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
        SQLiteDatabase db = warehousedb.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)){
            case WAREHOUSE_TABLE:{
                rowsDeleted=db.delete(warehouseContract.warehouseEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case WAREHOUSE_ITEM:{
                selection= warehouseContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(warehouseContract.warehouseEntry.TABLE_NAME,selection,selectionArgs);
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
            case WAREHOUSE_TABLE:{
                return updateReminder(uri,contentValues, selection, selectionArgs);
            }
            case WAREHOUSE_ITEM:{
                selection= warehouseContract.warehouseEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateReminder(uri,contentValues, selection, selectionArgs);
            }
            default:{
                throw new IllegalArgumentException("Unable to update unknown URI: " + uri);
            }
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(warehouseContract.warehouseEntry.COLUMN_EQUIPMENT_NAME)){
            String name = values.getAsString(warehouseContract.warehouseEntry.COLUMN_EQUIPMENT_NAME);
            if(name==null){
                throw new IllegalArgumentException("Equipment name is required field!");
            }
        }
        if(values.containsKey(warehouseContract.warehouseEntry.COLUMN_NEXT_INSPECTION_DATE)){
            String date = values.getAsString(warehouseContract.warehouseEntry.COLUMN_NEXT_INSPECTION_DATE);
            if(date==null){
                throw new IllegalArgumentException("Next inspection date is required field!");
            }
        }
        SQLiteDatabase db = warehousedb.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return db.update(warehouseContract.warehouseEntry.TABLE_NAME,values,selection,selectionArgs);
    }

}
