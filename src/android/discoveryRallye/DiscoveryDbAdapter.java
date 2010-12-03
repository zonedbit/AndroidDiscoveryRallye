package android.discoveryRallye;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiscoveryDbAdapter {
	
	/*
	 * Name of the database, table of the pois and the version
	 * of the database 
	 */
    private static final String DB_NAME         = "discoveryRallye";
    private static final String DB_TABLE_POIS   = "pois";
    private static final int    DB_VERSION 		= 1;
    
    /* The database attributes since version 1*/
    private static final String ATTR_ID   = "_id";
    public static final String ATTR_NAME = "name";
    private static final String ATTR_LON  = "longitude";
    private static final String ATTR_LAT  = "latitude";
    
    /* Database statements since version 1*/
    private static final String STMT_CREATE = 
    	"create table " + DB_TABLE_POIS 
    					+ " ("
    					+ 		ATTR_ID   + " integer primary key autoincrement, "
    					+ 	    ATTR_NAME + " text   not null, " 
    					+       ATTR_LON  + " double not null, "
    					+       ATTR_LAT  + " double not null  "
    					+ ");";
    private static final String STMT_DROP_POIS =   " DROP TABLE IF EXISTS "
    											+   DB_TABLE_POIS;
    
    /* Attributes for database access */
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sldb;
    
    /* The Android context */
    private final Context ctx;
    
    /* inner class */
    private static class DatabaseHelper extends SQLiteOpenHelper{
    	public DatabaseHelper(Context context) {
    		super(context, DB_NAME, null, DB_VERSION);
    	}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("DiscoveryRallye","DatabaseHelper::onCreate()");
			db.execSQL(STMT_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("DiscoveryRallye","DatabaseHelper::onUpgrade()");
			db.execSQL(STMT_DROP_POIS);
		}
    }

    /**
     * Default Constructor  
     * 
     * The Android context is necessary to open and close
     * the SQLite database.
     * 
     * @param ctx The Android context
     */
    // TODO Maybe private
    public DiscoveryDbAdapter(Context ctx){
    	this.ctx = ctx; 
    }
    
    // TODO Maybe static
    public DiscoveryDbAdapter open() throws SQLiteException{
    	dbHelper = new DatabaseHelper(ctx);
    	sldb = dbHelper.getWritableDatabase();
    	return this;
    }
    
    // TODO Comment me
    public void close(){
    	dbHelper.close();
    }
    
    // TODO Comment me
    // TODO Param POI Object
    public long insertPoi(){
    	// TODO Delete me
    	Log.i("DiscoveryRallye","DiscoveryDbAdapter::insertPoi()");
    	ContentValues val = new ContentValues();
    	val.put( ATTR_NAME, "FHDO");
    	val.put(ATTR_LON, "51.494995");
    	val.put(ATTR_LAT, "7.419649");
    	
    	return sldb.insert(DB_TABLE_POIS, null, val);
    }
    
    // TODO Comment me
    public Cursor getNotes(){
    	return sldb.query(DB_TABLE_POIS, new String[] {ATTR_ID, ATTR_NAME,
                ATTR_LON, ATTR_LAT}, null, null, null, null, null);
    }

}
