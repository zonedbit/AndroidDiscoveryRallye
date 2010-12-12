package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CampusOSM extends Activity {

	private static final String TAG = CampusOSM.class.getName();
	private static OpenStreetMapView openStreetMapView;
	private OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> itemizedOverlay;
	private ResourceProxy mResourceProxy;
	
	//Emil-Figge Straße 42
	GeoPoint fb4 = new GeoPoint(51494995, 7419649);
	
	private static ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	
	private RelativeLayout relativeLayout;
	private static ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();
	private MyLocationOverlay myLocationOverlay;
	private POI destination;
	
	public static ArrayList<OpenStreetMapViewOverlayItem> getItems()
	{
		return items;
	}
	
	public static ArrayList<GeoPoint> getGeoPoints()
	{
		return geopoints;
	}
	
	public static OpenStreetMapView getOpenStreetMapView()
	{
		return openStreetMapView;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openStreetMapView = new OpenStreetMapView(this);
        this.mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
    	POIContainer poiContainer = POIContainer.getInstance(getBaseContext());
    	ArrayList<POI> allPOIs = poiContainer.getALLPOIs();
    	
    	//initiales Füllen der Karte mit Items
    	for (POI poi : allPOIs) {
    		OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem(poi.getDescription(), poi.getDescription(), new GeoPoint(poi.getLat(), poi.getLon()));
			items.add(openStreetMapViewOverlayItem);
		}
    	
        createLayout();
        addMyLocationOverlay();
        addItemizedOverlay();
        setInitialView();
        setPreferences();
        
        Intent intent = getIntent();
        
        Bundle bundle = intent.getExtras();
        
        //Die Karte wird mit einem neuen POI aufgerufen. Dabei soll die Route gezeichnet werden
        if(bundle != null)
        {
			geopoints = (ArrayList<GeoPoint>) bundle.get("pois");
        	destination =  (POI) bundle.get("destination");
        	RouteOverlay routeOverlay = new RouteOverlay(geopoints, openStreetMapView, this);
			openStreetMapView.getOverlays().add(routeOverlay);
        }
    }
    
    
    private void addMyLocationOverlay() {
    	
	        myLocationOverlay = new MyLocationOverlay(this.getBaseContext(), openStreetMapView, mResourceProxy);
	        
	        //dadurch folgt die Map der Position
	        myLocationOverlay.followLocation(true);
	        
	        //Batterie sparen
	        myLocationOverlay.setLocationUpdateMinDistance(50);
	        myLocationOverlay.setLocationUpdateMinTime(1000);
	        openStreetMapView.getOverlays().add(myLocationOverlay);
	}
    
    @Override
    protected void onPause() {
    	//Batterie sparen
    	myLocationOverlay.disableMyLocation();
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	myLocationOverlay.enableMyLocation();
    	super.onResume();
    }

	private void addItemizedOverlay() 
    {
	        this.itemizedOverlay = new OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem>(this, items, new ItemGestureListener<OpenStreetMapViewOverlayItem>(), mResourceProxy);
	        this.itemizedOverlay.setFocusItemsOnTap(false);
	        //this.itemizedOverlay.setFocusedItem(0); //TODO: Dadurch zoomt er erst zur FH und dann zur später zur eigenen Position -> Gewollt?
	        openStreetMapView.getOverlays().add(this.itemizedOverlay);
	}
    
    public void addItem(double latitude, double longitude)
    {
    	Log.d(TAG, "");
    	NewPOIDialog dlg = new NewPOIDialog(this, latitude, longitude, items, openStreetMapView);
		dlg.show();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
    	return super.onTouchEvent(event);
    }
    
	private void setPreferences() 
    {
    	openStreetMapView.setBuiltInZoomControls(true);
	}

	/*
     * Hier wird die grafische Oberfläche zusammengebaut. Eine Variante über XML ist mir nicht bekannt.
     */
    public void createLayout()
    {
    	relativeLayout = new RelativeLayout(this);
    	//TOOD: Ob ich das nicht in das XML übertragen kann?
        relativeLayout.addView(openStreetMapView, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.setContentView(relativeLayout);
    }
    
    public void setInitialView()
    {
    	openStreetMapView.getController().setZoom(16);
    	
    	if(myLocationOverlay.getMyLocation() != null)
    	{
    		openStreetMapView.getController().setCenter(myLocationOverlay.getMyLocation());
    	}
    	else
    	{
    		Location retrieveLastKnownLocation = GeoUtils.retrieveLastKnownLocation(this);
    		openStreetMapView.getController().setCenter(GeoUtils.createGeoPoint(retrieveLastKnownLocation));
    	}
    	openStreetMapView.invalidate();
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, 1, Menu.FIRST, "Zeige Standort");
    	menu.add(0, 2, Menu.NONE, "Speichere aktuelle Location");
    	menu.add(0, 3, Menu.NONE, "Zeichne die Route neu");
		return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	GeoPoint userLocation = myLocationOverlay.getMyLocation();
    	
    	switch (item.getItemId()) {
		case 1:
			if(userLocation != null)
			{
				openStreetMapView.getController().setCenter(myLocationOverlay.getMyLocation());
				return true;
			}
			
		case 2:
			if(userLocation != null)
			{
				addItem(userLocation.getLatitudeE6() / 1E6, userLocation.getLongitudeE6() / 1E6);
			}
			return true;
		case 3:
			JSONRequest jsonRequest = new JSONRequest(this, destination);
			jsonRequest.calculateRoute();
			return true;
		}
		return false;
    }
}
