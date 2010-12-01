package android.discoveryRallye;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.views.overlay.OpenStreetMapViewSimpleLocationOverlay;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class CampusOSM extends Activity {

	private OpenStreetMapView openStreetMapView;
	private OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> itemizedOverlay;
	private ResourceProxy mResourceProxy;
	
	//Emil-Figge Straße 42
	private final Double LAT = 51.495057;
	private final Double LON = 7.419638;
	
	private RelativeLayout relativeLayout;
	private ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();
	private MyLocationOverlay myLocationOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.openStreetMapView = new OpenStreetMapView(this);
        this.mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
        //ein Item  muss mindestens dabei, sonst kommt der ItemizedOverlay nicht klar
        OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem("Berlin", "This is a relatively short SampleDescription.", new GeoPoint(52518333, 13408333));
        items.add(openStreetMapViewOverlayItem);
        
        addCampusOverlay();
        
        addMyLocationOverlay();
        setPreferences();
        setInitialView();
        createLayout();
    }
    
    
    private void addMyLocationOverlay() {
	        this.myLocationOverlay = new MyLocationOverlay(this.getBaseContext(), openStreetMapView, mResourceProxy);
	        //Batterie sparen
	        myLocationOverlay.setLocationUpdateMinDistance(100);
	        myLocationOverlay.setLocationUpdateMinTime(4000);
	        this.openStreetMapView.getOverlays().add(this.myLocationOverlay);
	}
    
    @Override
    protected void onPause() {
    	//Batterie sparen
    	myLocationOverlay.disableMyLocation();
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	this.myLocationOverlay.enableMyLocation();
    	super.onResume();
    }

	private void addRouteOverlay()
    {
		POI myLocation = new POI(51.494995, 7.419649, "FB4");
		POI destination = new POI(51.515872, 7.466852, "zuhause" );
		
		addItem(new GeoPoint(myLocation.getLat(), myLocation.getLon()), myLocation.getDescription());
		addItem(new GeoPoint(destination.getLat(), destination.getLon()), destination.getDescription());
		
		//TODO: Das muss man unbedingt in einen ProgressBar/Thread stecken
		JSONRequest jsonRequest = new JSONRequest(this, openStreetMapView);
		 
		ArrayList<POI> pois = new ArrayList<POI>();
		pois.add(myLocation);
		pois.add(destination);
		 
		 @SuppressWarnings("unchecked")
		AsyncTask<ArrayList<POI>, Void, ArrayList<GeoPoint>> jsonResponse = jsonRequest.execute(pois);
	}
	
	public void calculateRoute(POI destination)
	{
		
	}

	private void addCampusOverlay() 
    {
	        this.itemizedOverlay = new OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem>(this, items, new ItemGestureListener<OpenStreetMapViewOverlayItem>(), mResourceProxy);
	        this.itemizedOverlay.setFocusItemsOnTap(true);
	        this.itemizedOverlay.setFocusedItem(0); //TODO
	        
	        this.openStreetMapView.getOverlays().add(this.itemizedOverlay);
	}
    
    public void addItem(GeoPoint geoPoint, String description)
    {
    	//TODO: Long- und Shortdescription benutzen?
    	OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem(description, description, geoPoint);
    	items.add(openStreetMapViewOverlayItem);
    	this.openStreetMapView.invalidate(); //neu zeichnen
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
    	return super.onTouchEvent(event);
    }
    
	private void setPreferences() 
    {
    	this.openStreetMapView.setBuiltInZoomControls(true);
	}

	/*
     * Hier wird die grafische Oberfläche zusammengebaut. Eine Variante über XML ist mir nicht bekannt.
     */
    public void createLayout()
    {
    	relativeLayout = new RelativeLayout(this);
    	//TOOD: Ob ich das nicht in das XML übertragen kann?
        relativeLayout.addView(this.openStreetMapView, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.setContentView(relativeLayout);
    }
    
    /*
     * Die Karten wird auf Dortmund zentriert und es wird rangezoomt
     */
    public void setInitialView()
    {
        GeoPoint geoPoint = new GeoPoint(LAT, LON);
    	
    	this.openStreetMapView.getController().setZoom(16);
    	//Alternativ könnte man auch auf die Postion des Benutzer zoomen -> Wäre das gewollt?
    	this.openStreetMapView.getController().setCenter(geoPoint);
    	this.openStreetMapView.invalidate();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, 1, Menu.FIRST, "Test Route");
    	menu.add(0, 2, Menu.NONE, "Test Save Position");
		return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	switch (item.getItemId()) {
		case 1:
			addRouteOverlay();
			return true;
		case 2:
			saveLocation();
			return true;
		}
		return false;
    }


	private void saveLocation() 
	{
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Location lastKnownLocation = locationManager.getLastKnownLocation(getBestProvider(locationManager));

		//TODO: Das muss noch entsprechend gefüllt werden!
		POI poi = new POI(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), "My Location");
		
		//TODO: In der Datenbank speichern
		
		//Und das Item der Karte hinzufügen
		addItem(new GeoPoint(poi.getLat(), poi.getLon()), poi.getDescription());
		
	}
	
	private String getBestProvider(LocationManager locationManager)
	{
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		return locationManager.getBestProvider(criteria, true);
	}
}
