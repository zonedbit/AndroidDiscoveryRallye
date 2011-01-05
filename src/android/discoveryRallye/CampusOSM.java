package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;
import org.andnav.osm.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CampusOSM extends Activity {

	private static final String TAG = CampusOSM.class.getName();
	private static OpenStreetMapView openStreetMapView;
	private OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> itemizedOverlay;
	private ResourceProxy mResourceProxy;
	
	//Emil-Figge Straße 42
	GeoPoint fb4 = new GeoPoint(51494995, 7419649);
	
	private ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	
	private RelativeLayout relativeLayout;
	private static ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();
	private static MyLocationOverlay myLocationOverlay;
	private POI destination = null;
	private ScaleBarOverlay scaleBarOverlay;
	private static RouteOverlay routeOverlay = null;
	
	private static int rallyeState = 0;
	
	public static ArrayList<OpenStreetMapViewOverlayItem> getItems()
	{
		return items;
	}
	
	public static OpenStreetMapView getOpenStreetMapView()
	{
		return openStreetMapView;
	}
	
	public static MyLocationOverlay getMyLocationOverlay()
	{
		return myLocationOverlay;
	}
	
	public static RouteOverlay getRouteOverlay()
	{
		return routeOverlay;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openStreetMapView = new OpenStreetMapView(this);
        this.mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
    	POIContainer poiContainer = POIContainer.getInstance(getBaseContext());
    	ArrayList<POI> allPOIs = poiContainer.getALLPOIs();
    	
    	//initiales Füllen der Karte mit Items. Dazu brauche ich z.B. die Default-Daten. Der Overlay braucht mindestens ein Item.
    	items.clear();
    	for (POI poi : allPOIs) 
    	{
    		OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem(poi.getDescription(), "", new GeoPoint(poi.getLat(), poi.getLon()));
			items.add(openStreetMapViewOverlayItem);
		}
    	
        createLayout();
        addMyLocationOverlay();
        addItemizedOverlay();
        addScaleBar();
        setInitialView();
        setPreferences();
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        
        //Die Karte wird mit einem neuen POI aufgerufen. Dabei soll die Route gezeichnet werden
        if(bundle != null)
        {
			geopoints = (ArrayList<GeoPoint>) bundle.get("pois");
			destination = (POI) bundle.get("destination");
			openStreetMapView.getController().setCenter(geopoints.get(0));
			addRouteOverlay();
        }
    }
    
    private void addRouteOverlay()
    {
		routeOverlay = new RouteOverlay(geopoints, openStreetMapView, this, Color.YELLOW);
		openStreetMapView.getOverlays().add(routeOverlay);
    }
    
    
    private void addScaleBar() {
    	this.scaleBarOverlay = new ScaleBarOverlay(this, mResourceProxy);
    	CampusOSM.openStreetMapView.getOverlays().add(scaleBarOverlay);
    	this.scaleBarOverlay.setScaleBarOffset(scaleBarOverlay.screenWidth/2 - scaleBarOverlay.xdpi/2, 10);
    	this.scaleBarOverlay.setMetric();
	}

	private void addMyLocationOverlay() {
    	
	        myLocationOverlay = new MyLocationOverlay(this.getBaseContext(), openStreetMapView, mResourceProxy);
	        
	        //dadurch folgt die Map der Position
	        myLocationOverlay.followLocation(true);
	        
	        //Provider starten hier durch
	        myLocationOverlay.enableMyLocation();
	        
	        //und ein Kompass soll her
	        myLocationOverlay.enableCompass();
	        
	        //Batterie sparen
	        myLocationOverlay.setLocationUpdateMinDistance(50);
	        myLocationOverlay.setLocationUpdateMinTime(100);
	        openStreetMapView.getOverlays().add(myLocationOverlay);
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
    	//Batterie sparen
    	myLocationOverlay.disableMyLocation();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	myLocationOverlay.enableMyLocation();
    }
    
	private void addItemizedOverlay() 
    {
	        this.itemizedOverlay = new OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem>(this, items, new ItemGestureListener<OpenStreetMapViewOverlayItem>(), mResourceProxy);
	        this.itemizedOverlay.setFocusItemsOnTap(true);
	        openStreetMapView.getOverlays().add(this.itemizedOverlay);
	}
    
    public void addItem(double latitude, double longitude)
    {
    	NewPOIDialog dlg = new NewPOIDialog(this, latitude, longitude, items, openStreetMapView);
		dlg.show();
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
    	openStreetMapView.getController().setZoom(12);
    	
    	if(myLocationOverlay.getMyLocation() != null)
    	{
    		openStreetMapView.getController().setCenter(myLocationOverlay.getMyLocation());
    	}
    	else
    	{
    		openStreetMapView.getController().setCenter(fb4);
    	}
    	openStreetMapView.invalidate();
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, 1, Menu.FIRST, "Zeige Standort");
    	menu.add(0, 2, Menu.NONE, "Speichere aktuelle Location");
    	menu.add(0, 3, Menu.NONE, "Zeichne deine gesuchte Route neu");
    	menu.add(0, 4, Menu.NONE, "Starte Rallye/Nächstes Ziel");
    	menu.add(0, 5, Menu.NONE, "Neue Wegbeschreibung für die Rallye");
		return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	GeoPoint userLocation = myLocationOverlay.getMyLocation();
    	
    	switch (item.getItemId()) 
    	{
		case 1:
			if(userLocation != null)
			{
				openStreetMapView.getController().setCenter(myLocationOverlay.getMyLocation());
				openStreetMapView.getController().setZoom(15);
				return true;
			}
			return false;
		case 2:
			if(userLocation != null)
			{
				addItem(userLocation.getLatitudeE6() / 1E6, userLocation.getLongitudeE6() / 1E6);
				return true;
			}
			return false;
		case 3:
			JSONRequest jsonRequest = new JSONRequest(this, destination);
			jsonRequest.calculateRoute();
			return true;
		case 4:
			if(rallyeState != DiscoveryDbAdapter.staticPois.length)
			{
				Builder builder = new Builder(this);
				builder.setTitle("Ziel");
				builder.setMessage("Das Ziel ist: " + DiscoveryDbAdapter.staticPois[rallyeState].getDescription());
				builder.setCancelable(false);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						JSONRequest jsonRequestForNextRallyeWayPoint = new JSONRequest(CampusOSM.this, DiscoveryDbAdapter.staticPois[rallyeState]);
						jsonRequestForNextRallyeWayPoint.calculateRoute();
						rallyeState++;
					}
				});
				
				builder.create();
				builder.show();
			}
			else
			{
				Builder builder = new Builder(this);
				builder.setTitle("Die Rallye wurde bereits durchlaufen.");
				builder.setMessage("Soll die Rallye von vorne beginnen?");
				builder.setCancelable(false);
				builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						rallyeState = 0;
					}
				});
				
				builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				});
				
				builder.create();
				builder.show();
			}
			return false;
		case 5: 
			if(rallyeState != DiscoveryDbAdapter.staticPois.length)
			{
				if(rallyeState > 0)
				{
					JSONRequest jsonRequestForCurrentRallyeWayPoint = new JSONRequest(this, DiscoveryDbAdapter.staticPois[rallyeState - 1]);
					jsonRequestForCurrentRallyeWayPoint.calculateRoute();
					return true;
				}
				else
				{
					Builder builder = new Builder(this);
					builder.setTitle("Keine Route vorhanden");
					builder.setMessage("Die Rallye hat noch nicht begonnen. Bitte starte erst die Rallye!");
					builder.setCancelable(false);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
						}
					});
					
					builder.create();
					builder.show();
				}
			}
		}
		return false;
    }
}
