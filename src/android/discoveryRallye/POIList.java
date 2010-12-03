package android.discoveryRallye;

import java.util.ArrayList;

import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class POIList extends ListActivity {
	private POIContainer poic;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		poic = POIContainer.getInstance(getBaseContext());
		this.fillList();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		POI poi = poic.getPOI(position);
		// poic.addPOI(new POI(51.493670, 7.420191, "Fake " +position ));
		poic.removePOI(poi.getDescription(), id);
		fillList(); // refresh the view

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

	// TODO Comment me
	private void fillList() {
		poic.getALLPOIs();
		setListAdapter((ListAdapter) new ArrayAdapter<String>(this,
				R.layout.poi_row, poic.getAllPOIsName()));
	}

}
