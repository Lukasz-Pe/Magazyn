package com.egm.magazyn.data.dbproviders.reminders;

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

public class remindersProvider extends ContentProvider {

    private static final String LOG_TAG=remindersProvider.class.getSimpleName();
    private remindersDBHelper remindersdb;
    //IDs for UriMatcher
    private static final int REMINDERS_TABLE =100, REMINDER_ITEM =101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Database and provider helper objects
    static{
        sUriMatcher.addURI(remindersContract.CONTENT_AUTHORITY, remindersContract.remindersEntry.TABLE_NAME, REMINDERS_TABLE);
        sUriMatcher.addURI(remindersContract.CONTENT_AUTHORITY, remindersContract.remindersEntry.TABLE_NAME+"/#", REMINDER_ITEM);
    }

    @Override
    public boolean onCreate() {
        remindersdb=new remindersDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=remindersdb.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        Log.i("uri",uri.toString());
        Log.i("URIMatcher", String.valueOf(match));
        switch(match){
            case REMINDERS_TABLE:{
                cursor=db.query(remindersContract.remindersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            }
            case REMINDER_ITEM:{
                selection=remindersContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(remindersContract.remindersEntry.TABLE_NAME,
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
                return remindersContract.remindersEntry.CONTENT_LIST_TYPE;
            }
            case REMINDER_ITEM:{
                return remindersContract.remindersEntry.CONTENT_ITEM_TYPE;
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
                SQLiteDatabase db = remindersdb.getReadableDatabase();
                String name = contentValues.getAsString(remindersContract.remindersEntry.COLUMN_EQUIPMENT_NAME);
                String date = contentValues.getAsString(remindersContract.remindersEntry.COLUMN_NEXT_INSPECTION_DATE);
                if (name == null) {
                    throw new IllegalArgumentException("Name is required field!");
                }
                if(date==null){
                    throw new IllegalArgumentException("Next inspection date is required field!");
                }
                getContext().getContentResolver().notifyChange(uri, null);
                long id = db.insert(remindersContract.remindersEntry.TABLE_NAME, null, contentValues);
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
        SQLiteDatabase db = remindersdb.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)){
            case REMINDERS_TABLE:{
                rowsDeleted=db.delete(remindersContract.remindersEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case REMINDER_ITEM:{
                selection=remindersContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(remindersContract.remindersEntry.TABLE_NAME,selection,selectionArgs);
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
                selection= remindersContract.remindersEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateReminder(uri,contentValues, selection, selectionArgs);
            }
            default:{
                throw new IllegalArgumentException("Unable to update unknown URI: " + uri);
            }
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(remindersContract.remindersEntry.COLUMN_EQUIPMENT_NAME)){
            String name = values.getAsString(remindersContract.remindersEntry.COLUMN_EQUIPMENT_NAME);
            if(name==null){
                throw new IllegalArgumentException("Equipment name is required field!");
            }
        }
        if(values.containsKey(remindersContract.remindersEntry.COLUMN_NEXT_INSPECTION_DATE)){
            String date = values.getAsString(remindersContract.remindersEntry.COLUMN_NEXT_INSPECTION_DATE);
            if(date==null){
                throw new IllegalArgumentException("Next inspection date is required field!");
            }
        }
        SQLiteDatabase db = remindersdb.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);

        return db.update(remindersContract.remindersEntry.TABLE_NAME,values,selection,selectionArgs);
    }

}
