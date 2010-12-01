package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class CampusOSM extends Activity {

	private static OpenStreetMapView openStreetMapView;
	private OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> itemizedOverlay;
	private ResourceProxy mResourceProxy;
	
	//Emil-Figge Straße 42
	GeoPoint fb4 = new GeoPoint(51494995, 7419649);
	
	private RelativeLayout relativeLayout;
	private ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();
	private MyLocationOverlay myLocationOverlay;
	
	public static OpenStreetMapView getOpenStreetMapView()
	{
		return openStreetMapView;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CampusOSM.openStreetMapView = new OpenStreetMapView(this);
        this.mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
        //ein Item  muss mindestens dabei, sonst kommt der ItemizedOverlay nicht klar
    	OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem("Dortmund", "Fachbereich 4 - Informatik", fb4);
    	items.add(openStreetMapViewOverlayItem);
        	
        createLayout();
        addItemizedOverlay();
        addMyLocationOverlay();
        setInitialView();
        setPreferences();
        
        Intent intent = getIntent();
        
        Bundle bundle = intent.getExtras();
        
        //Die Karte wird mit einem neuen POI aufgerufen. Dabei soll die Route gezeichnet werden
        if(bundle != null)
        {
        	@SuppressWarnings("unchecked")
			ArrayList<GeoPoint> geoPoints = (ArrayList<GeoPoint>) bundle.get("poi");
        	RouteOverlay routeOverlay = new RouteOverlay(geoPoints, openStreetMapView, this);
			openStreetMapView.getOverlays().add(routeOverlay);
        }
    }
    
    
    private void addMyLocationOverlay() {
	        this.myLocationOverlay = new MyLocationOverlay(this.getBaseContext(), openStreetMapView, mResourceProxy);
	        
	        //dadurch folgt die Map der Position
	        myLocationOverlay.followLocation(true);
	        
	        //Batterie sparen
	        myLocationOverlay.setLocationUpdateMinDistance(100);
	        myLocationOverlay.setLocationUpdateMinTime(6000);
	        CampusOSM.openStreetMapView.getOverlays().add(this.myLocationOverlay);
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

	private void addItemizedOverlay() 
    {
	        this.itemizedOverlay = new OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem>(this, items, new ItemGestureListener<OpenStreetMapViewOverlayItem>(), mResourceProxy);
	        this.itemizedOverlay.setFocusItemsOnTap(true);
	        this.itemizedOverlay.setFocusedItem(0); //TODO: Dadurch zoomt er erst zur FH und dann zur später zur eigenen Position -> Gewollt?
	        CampusOSM.openStreetMapView.getOverlays().add(this.itemizedOverlay);
	}
    
    public void addItem(GeoPoint geoPoint, String description)
    {
    	//TODO: Long- und Shortdescription benutzen?
    	OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem(description, description, geoPoint);
    	items.add(openStreetMapViewOverlayItem);
    	CampusOSM.openStreetMapView.invalidate(); //neu zeichnen
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
    	return super.onTouchEvent(event);
    }
    
	private void setPreferences() 
    {
    	CampusOSM.openStreetMapView.setBuiltInZoomControls(true);
	}

	/*
     * Hier wird die grafische Oberfläche zusammengebaut. Eine Variante über XML ist mir nicht bekannt.
     */
    public void createLayout()
    {
    	relativeLayout = new RelativeLayout(this);
    	//TOOD: Ob ich das nicht in das XML übertragen kann?
        relativeLayout.addView(CampusOSM.openStreetMapView, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.setContentView(relativeLayout);
    }
    
    /*
     * Die Karten wird auf den Fachbereich 4 zentriert und es wird rangezoomt
     */
    public void setInitialView()
    {
    	CampusOSM.openStreetMapView.getController().setZoom(16);
    	CampusOSM.openStreetMapView.getController().setCenter(fb4);
    	CampusOSM.openStreetMapView.invalidate();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, 1, Menu.FIRST, "Zeige Standort");
    	menu.add(0, 2, Menu.NONE, "Test Save Position");
		return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	switch (item.getItemId()) {
		case 1:
			Location retrieveLocation = GeoUtils.retrieveLocation(this);
			GeoPoint geoPoint = new GeoPoint(retrieveLocation.getLatitude(), retrieveLocation.getLongitude());
			CampusOSM.openStreetMapView.getController().setCenter(geoPoint);
			return true;
		case 2:
			Location retrievedLocation = GeoUtils.retrieveLocation(this);
			//TODO: Das muss noch mit einer Beschreibung gefüllt werden
			//TODO: In der Datenbank speichern
			POI poi = new POI(retrievedLocation.getLatitude(), retrievedLocation.getLongitude(), "My Location");
			distributePOI(poi);
			
			return true;
		}
		return false;
    }
    
    private void distributePOI(POI poi)
    {
    	addItem(new GeoPoint(poi.getLat(), poi.getLon()), poi.getDescription());
    	POIList.addListItem(poi);
    }
}
