package android.discoveryRallye;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class POIList extends ListActivity 
{
	private static ArrayList<POI> pois = new ArrayList<POI>();
	private static ArrayList<String> poisDescription = new ArrayList<String>();
	
	private DiscoveryDbAdapter db;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//TODO: Test
		addListItem(new POI(51.494995, 7.419649, "FB4" ));
		
		setListAdapter((ListAdapter)  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, poisDescription));
		
		// TODO Delete just testing
		this.db = new DiscoveryDbAdapter(this);
		db.open();
		db.insertPoi();
		this.fillList();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		POI poi = pois.get(position);
		
		addRouteOverlay(poi);
	}
	
	//TODO: Dieses statische Rumgeschiebe könnte man durch die Datenbank umgehen
	//Dann braucht diese Methode so nicht mehr.
	public static void addListItem(POI poi)
	{
		if(!poisDescription.contains(poi.getDescription()))
		{
			poisDescription.add(poi.getDescription());
			pois.add(poi);
		}
		else
		{
			//TODO: Fehlermeldung: POI schon enthalten
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addRouteOverlay(POI destination)
    {
		JSONRequest jsonRequest = new JSONRequest(this);
		 
		ArrayList<POI> pois = new ArrayList<POI>();
		
		Location retrievedLocation = GeoUtils.retrieveLocation(this);
		POI myLocation = new POI(retrievedLocation.getLatitude(), retrievedLocation.getLongitude(), "My Location");
		
		pois.add(myLocation);
		pois.add(destination);
		 
		jsonRequest.execute(pois);
	}
	
	// TODO Comment me
	private void fillList(){
    	// TODO Delete me
		Log.i("DiscoveryRallye","POIList::fillList()");
        Cursor c = db.getNotes();
        startManagingCursor(c);

        String[] src = new String[] { DiscoveryDbAdapter.ATTR_NAME };
        int[]    dst = new int[] { R.id.poiRowTxt };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter pois =
            new SimpleCursorAdapter(this, R.layout.poi_row, c, src, dst);
        setListAdapter(pois);
		
	}
	
	
}
