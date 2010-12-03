package android.discoveryRallye;

public interface IDBPOI {
	/*
	 * Name of the database, table of the pois and the version
	 * of the database 
	 */
    static final String DB_NAME         = "discoveryRallye";
    static final String DB_TABLE_POIS   = "pois";
    static final int    DB_VERSION 		= 1;
    
    /* The database attributes since version 1*/
    static final String ATTR_ID    = "_id";
    static final  String ATTR_NAME = "name";
    static final String ATTR_LON   = "longitude";
    static final String ATTR_LAT   = "latitude";

}
