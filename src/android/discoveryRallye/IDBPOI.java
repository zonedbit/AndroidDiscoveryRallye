package android.discoveryRallye;

/**
 * \brief Static SQLite variables
 * 
 * Interface thats just define some static variables
 * for the SQLite database.
 * 
 * @author axel
 *
 */
public interface IDBPOI {
	/** Name of the database */
    static final String DB_NAME         = "discoveryRallye";
    
    /** Name of the table */
    static final String DB_TABLE_POIS   = "pois";
    
    /** version number of the database */
    static final int    DB_VERSION 		= 1;
    
    /** name of the id column */
    static final String ATTR_ID    = "_id";
    
    /** name of the name column */
    static final String ATTR_NAME = "name";
    
    /** name of the longitude column */
    static final String ATTR_LON   = "longitude";
    
    /** name of the latitude column */
    static final String ATTR_LAT   = "latitude";
}
