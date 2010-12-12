package android.discoveryRallye;

import org.andnav.osm.util.GeoPoint;

import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class POIList extends ListActivity 
					 implements IUIRefreshable, IOverlayRouteable {
	private POIContainer poic;

	/**
	 * Create this activity
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		poic = POIContainer.getInstance(getBaseContext());
		this.uiRefresh();
	}
	

	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		/* Create the custom dialog*/		
		POIDialog dlg = new POIDialog(this, this, this, position);
		dlg.show();
		
//		long result = poic.addPOI(new POI(51.493670, 7.420191, "Fake " +position ));
//		if ( result <= 0){
//			Toast.makeText(this, "POI bereits vorhanden", Toast.LENGTH_SHORT)
//				.show();
//		}
	}

	public void addRouteOverlay(POI destination) 
	{
		GeoUtils.activateLocationListener(this);
		JSONRequest jsonRequest = new JSONRequest(this, destination);
		jsonRequest.calculateRoute();
	}

	/**
	 * Create the UI-List. After adding a POI or several POI to 
	 * the POICOntainer call this method to refresh the UI
	 * 
	 * @see POI
	 * @see POIContainer
	 */
	public void uiRefresh() {
		setListAdapter((ListAdapter) new ArrayAdapter<String>(this,R.layout.poi_row, poic.getAllPOIsName()));
	}
}
