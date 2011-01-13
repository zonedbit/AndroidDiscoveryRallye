package android.discoveryRallye;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * The UI class to present a list of POIs.
 * 
 * @author axel
 *
 */
public class POIList extends ListActivity implements IUIRefreshable,
		IOverlayRouteable {
	private POIContainer poic;

	/**
	 * Create this activity
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		poic = POIContainer.getInstance(getBaseContext());
		this.uiRefresh();
	}

	/**
	 * OnListItemClick listener
	 * 
	 * Open the POIDialog, to manipulate a POI object.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		/* Create the custom dialog */
		POIDialog dlg = new POIDialog(this, this, this, position);
		dlg.show();
	}

	/**
	 * Add the route overlay to the map and switch to the map tab
	 */
	public void addRouteOverlay(POI destination) {
		// GeoUtils.activateLocationListener(this);
		JSONRequest jsonRequest = new JSONRequest(this, destination);
		jsonRequest.calculateRoute();
	}

	/**
	 * Create the UI-List. After adding a POI or several POI to the POICOntainer
	 * call this method to refresh the UI
	 * 
	 * @see POI
	 * @see POIContainer
	 */
	public void uiRefresh() {
		setListAdapter((ListAdapter) new ArrayAdapter<String>(this,R.layout.poi_row, poic.getAllPOIsName()));
		CampusOSM.getItems().clear();
		
		GeoPoint greenwich = new GeoPoint(514773, 96);
		OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem("Greenwicj", "", greenwich);
		CampusOSM.getItems().add(openStreetMapViewOverlayItem);
		
		for(POI poi : DiscoveryDbAdapter.staticPois)
		{
			OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem2 = new OpenStreetMapViewOverlayItem(poi.getDescription(), "", new GeoPoint(poi.getLat(), poi.getLon()));
			CampusOSM.getItems().add(openStreetMapViewOverlayItem2);
		}
		CampusOSM.getOpenStreetMapView().invalidate();
	}

	/**
	 * The Option menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, Menu.FIRST, "POIs zurücksetzen");
		return true;
	}

	/**
	 * Option menu item listener
	 * 
	 * Reset the database and refresh the user interface
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = false;

		switch (item.getItemId()) {
		case 1:
			//Re-Init the Container
			POIContainer.getInstance(this).reInit();
			uiRefresh();
			result = true;
			break;
		}

		return result;
	}
}
