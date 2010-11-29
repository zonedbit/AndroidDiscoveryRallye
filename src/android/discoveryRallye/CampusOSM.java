package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Activity;
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
	private OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> myLocationOverlay;
	private ResourceProxy mResourceProxy;
	
	//Emil-Figge Straße 42
	private final Double LAT = 51.495057;
	private final Double LON = 7.419638;
	
	private RelativeLayout relativeLayout;
	private ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.openStreetMapView = new OpenStreetMapView(this);
        this.mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
        //ein Item  muss mindestens dabei, sonst kommt der ItemizedOverlay nicht klar
        OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem("Berlin", "This is a relatively short SampleDescription.", new GeoPoint(52518333, 13408333));
        items.add(openStreetMapViewOverlayItem);
        
        addCampusOverlay();
        //addRouteOverlay();
        setPreferences();
        setInitialView();
        createLayout();
    }
    
    
    
    private void addRouteOverlay() 
    {
	}



	private void addCampusOverlay() 
    {
	        this.myLocationOverlay = new OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem>(this, items, new ItemGestureListener<OpenStreetMapViewOverlayItem>(), mResourceProxy);
	        this.myLocationOverlay.setFocusItemsOnTap(true);
	        this.myLocationOverlay.setFocusedItem(0); //TODO
	        
	        this.openStreetMapView.getOverlays().add(this.myLocationOverlay);
	}
    
    public void addItem(OpenStreetMapViewOverlayItem item)
    {
    	items.add(item);
    	this.openStreetMapView.invalidate(); //neu zeichnen
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
    	Toast.makeText(this, "aha", Toast.LENGTH_LONG);
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
    	this.openStreetMapView.getController().setCenter(geoPoint);
    	this.openStreetMapView.invalidate();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	menu.add(0, 1, Menu.FIRST, "Test");
		return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	switch (item.getItemId()) {
		case 1:
			OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem("Hannover", "Tiny SampleDescription", new GeoPoint(52370816, 9735936));
			addItem(openStreetMapViewOverlayItem);
			return true;
		}
		return false;
    }
}
