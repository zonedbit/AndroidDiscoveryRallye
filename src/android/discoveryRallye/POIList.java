package android.discoveryRallye;

import java.util.ArrayList;

import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class POIList extends ListActivity implements IUIRefreshable {
	private POIContainer poic;

	/**
	 * Create this activity
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		poic = POIContainer.getInstance(getBaseContext());
		this.uiRefresh();
	}
	

	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		/* Create the custom dialog*/		
		POIDialog dlg = new POIDialog(this, this, position);
		dlg.show();
		
//		POI poi = poic.getPOI(position);
		
//		long result = poic.addPOI(new POI(51.493670, 7.420191, "Fake " +position ));
//		if ( result <= 0){
//			Toast.makeText(this, "POI bereits vorhanden", Toast.LENGTH_SHORT)
//				.show();
//		}
		
//		poic.removePOI(poi.getDescription(), id);
//		fillList(); // refresh the view

		// TODO remove comment
		// addRouteOverlay(poi);
	}

	@SuppressWarnings("unchecked")
	private void addRouteOverlay(POI destination) {
		JSONRequest jsonRequest = new JSONRequest(this);

		ArrayList<POI> pois = new ArrayList<POI>();

		Location retrievedLocation = GeoUtils.retrieveLocation(this);
		POI myLocation = new POI(retrievedLocation.getLatitude(),
				retrievedLocation.getLongitude(), "My Location");

		pois.add(myLocation);
		pois.add(destination);

		jsonRequest.execute(pois);
	}

	/**
	 * Create the UI-List. After adding a POI or several POI to 
	 * the POICOntainer call this method to refresh the UI
	 * 
	 * @see POI
	 * @see POIContainer
	 */
	@Override
	public void uiRefresh() {
		
		// TODO Ensure that poic isn't null
		setListAdapter((ListAdapter) new ArrayAdapter<String>(this,
				R.layout.poi_row, poic.getAllPOIsName()));
	}
}
