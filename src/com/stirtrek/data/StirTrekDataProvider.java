package com.stirtrek.data;

import com.stirtrek.model.Interest;
import com.stirtrek.model.Interest.Interests;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class StirTrekDataProvider extends ContentProvider{

	private static final String TAG = "StirTrekDataProvider";
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "stirtrek.db";
    private static final String INTERESTS_TABLE_NAME = "interests";
	
    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + INTERESTS_TABLE_NAME + " ("
                    + Interests.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Interests.SESSIONID + " INTEGER,"
                    + Interests.TIMESLOTID + " INTEGER,"
                    + Interests.CREATED_ON + " INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    
    private DatabaseHelper _databaseHelper;
    
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		   SQLiteDatabase db = _databaseHelper.getWritableDatabase();

	        int count;
	        count = db.delete(INTERESTS_TABLE_NAME, where, whereArgs);

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } 
        else {
            values = new ContentValues();
        }

        if(values.containsKey(Interest.Interests.CREATED_ON) == false) {
        	Long now = Long.valueOf(System.currentTimeMillis());                        
        	values.put(Interests.CREATED_ON, now);
        }
        
        SQLiteDatabase db = _databaseHelper.getWritableDatabase();
        long rowId = db.insert(INTERESTS_TABLE_NAME, Interests.CREATED_ON, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Interests.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		_databaseHelper = new DatabaseHelper(getContext());
        return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(INTERESTS_TABLE_NAME);

        qb.setProjectionMap(Interests.sInterestsProjectionMap);
        
     // Get the database and run the query
        SQLiteDatabase db = _databaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = _databaseHelper.getWritableDatabase();

        int count;
        count = db.update(INTERESTS_TABLE_NAME, values, where, whereArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}		
}
