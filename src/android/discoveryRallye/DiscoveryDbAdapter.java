package android.discoveryRallye;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiscoveryDbAdapter implements IDBPOI{
    
    /* Database statements since version 1*/
    private static final String STMT_CREATE = 
    	"create table " + DB_TABLE_POIS 
    					+ " ("
    					+ 		ATTR_ID   + " integer primary key autoincrement, "
    					+ 	    ATTR_NAME + " text   unique  , " 
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
    public long insertPoi(POI poi){
    	// TODO Delete me
    	Log.i("DiscoveryRallye","DiscoveryDbAdapter::insertPoi()");
    	ContentValues val = new ContentValues();
    	val.put( ATTR_NAME, poi.getDescription());
    	val.put(ATTR_LON, 	poi.getLon());
    	val.put(ATTR_LAT, 	poi.getLat());
    	
    	return sldb.insert(DB_TABLE_POIS, null, val);
    }
    
    // TODO Comment me
    public boolean deletePOI(String name){
    	return 
    		sldb.delete(DB_TABLE_POIS, ATTR_NAME + "=" + "'"+name+"'", null) > 0;
    }
    
    // TODO Comment me
    public Cursor getPOIs(){
    	return sldb.query(DB_TABLE_POIS, new String[] {ATTR_ID, ATTR_NAME,
                ATTR_LON, ATTR_LAT}, null, null, null, null, null);
    }
    
    // TODO Comment me
    public void resetDB(){
    	// Drop table and create it again
    	sldb.execSQL(STMT_DROP_POIS);
    	sldb.execSQL(STMT_CREATE);
    	
    	// Set all defaults POIs
		insertPoi(new POI(51.493670, 7.420191, "FH FB Informatik" ));
		insertPoi(new POI(51.493396, 7.416286, "Sonnendeck" ));
		insertPoi(new POI(51.492748, 7.416855, "Uni Bibliothek" ));
		insertPoi(new POI(51.493009, 7.414805, "Uni Mensa" ));
    }

}
