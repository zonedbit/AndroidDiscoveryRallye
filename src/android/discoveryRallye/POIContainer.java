package android.discoveryRallye;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class POIContainer implements IDBPOI {
	
	/* ArrayList is not synchronized */
	private ArrayList<POI> pois;
	private ArrayList<String> poisName;
	private static POIContainer poiCon;
	private Context ctx;
	
	/**
	 * Private Constructor; Singleton pattern 
	 */
	private POIContainer(Context ctx){
		Log.i("SQLite","POIContainer::POIContainer");
		pois = new ArrayList<POI>();
		poisName = new ArrayList<String>();
		this.ctx = ctx;
		fillPois();
	}
	
	/**
	 * Method to get the Instance of the unique POIConatiner
	 * 
	 * @return The reference of the container class
	 */
	public static POIContainer getInstance(Context ctx){
		if (poiCon == null ){
			poiCon = new POIContainer(ctx);
		}
		return poiCon;
	}
	
	// TODO Comment me
	public void addPOI(POI poi){
		pois.add(poi);
		poisName.add(poi.getDescription());
	}
	
	// TODO Comment me
	public void addPOIdb(POI poi){
		addPOI(poi);
		DiscoveryDbAdapter db = new DiscoveryDbAdapter(ctx);
		db.open();
		
		if (db != null ){
			db.insertPoi(poi);
			db.close();
		}else{
			Log.e("DiscoveryRallye","POIContainer::addPOIdb()");
		}
	}
	
	//TODO Comment me
	public void removePOI(String name, long id){
		// Delete form the ArrayLists
		pois.remove((int)id);
		poisName.remove((int)id);
		
		// Delete from the Database
		DiscoveryDbAdapter db = new DiscoveryDbAdapter(ctx);
		db.open();
		if (db != null ){
			db.deletePOI(name);
			db.close();
		}else{
			Log.e("DiscoveryRallye","POIContainer::removePOI(long id)");
		}
	}
	
	// TODO Comment me
	public POI getPOI(int index){
		index = ( index <0 || index >= pois.size()) ? 0 : index;
		return pois.get(index);
	}
	
	// TODO Comment me
	public ArrayList<POI> getALLPOIs(){
		return pois;
	}
	
	// TODO comment me
	public ArrayList<String> getAllPOIsName(){
		return poisName;
	}
	
	// TODO Comment me
	public int numberOfPois(){
		return pois.size();
	}
	
	
	// TODO Comment me
	private void fillPois(){
		DiscoveryDbAdapter db = new DiscoveryDbAdapter(ctx);
		
		if ( db != null ){
			db.open();
			
			//TODO Delete me
//			db.resetDB();
			
			Cursor c = db.getPOIs();
			/* If c a vaild cursor */
			if ( c != null ){
				// The result set contains values
				if ( c.moveToFirst() ){
					
					// Set the capacity to 10 more than records are in the table
					pois.ensureCapacity(c.getCount()+10);
					
					/* The indices of the columns */
					int locationName = c.getColumnIndex(ATTR_NAME);
					int latitude	 = c.getColumnIndex(ATTR_LAT);
					int longitude	 = c.getColumnIndex(ATTR_LON);
					
					do{
						// retrieve the information
						String name = c.getString(locationName);
						double lat  = c.getDouble(latitude);
						double lon  = c.getDouble(longitude);
						
						// Adding the POI to the ArrayList
						pois.add( new POI(lat, lon, name) );
						poisName.add(name);
						
						// TODO Delete me
						Log.i("SQLite1","Name: " +name + "lat/lon " +
								lat + "/" + lon);
							
					}while(c.moveToNext());
				}
			}
			// Close the DB
			db.close();
		}
	}

}
