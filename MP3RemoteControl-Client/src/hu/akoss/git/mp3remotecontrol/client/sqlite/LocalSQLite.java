package hu.akoss.git.mp3remotecontrol.client.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSQLite extends SQLiteOpenHelper {

	public static LocalSQLite Instance;
	
	public LocalSQLite(Context context) {
        super(context, "mp3control", null, 1);
        
        System.err.println("LocalSQLite created");
    }

	public static String getValue(String key_)
	{
		System.err.println("Get Key: " + key_);
		if (Instance == null)
		{
			System.err.println("Instance not set, returning null");
			return null;
		}
		
		SQLiteDatabase readableDb = Instance.getReadableDatabase();
		
		Cursor results = readableDb.rawQuery(
				  "SELECT lookup_value "
				+ " FROM lookup "
				+ " WHERE lookup_key=?", 
				new String [] { key_ } );
        
		if (results.moveToFirst()) {
			System.err.println("Value: " + results.getString(0));
        	return results.getString(0);
        }
        
        return null;
	}
	
	public static void setValue(String key_, String value_)
	{
		if (Instance == null)
		{
			return;
		}
		
		SQLiteDatabase writableDb = Instance.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("lookup_key", key_);
		values.put("lookup_value", value_);
		
		if (getValue(key_) == null)
		{
			writableDb.insert("lookup", null, values);
		}
		else
		{
			writableDb.update("lookup", values, "lookup_key=?", new String[] { key_ });
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE lookup (lookup_key TEXT, lookup_value TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
