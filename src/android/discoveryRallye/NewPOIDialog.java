package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * \brief
 * Mit dieser Klasse wird ein Dialog zur Erstellung eines POIs erzeugt.
 */
public class NewPOIDialog extends Dialog {

	/** The context of the calling activity */
	private Context context;
	
	/** Reference to the POI container class*/
	POIContainer poic;
	
	/** 
	 * Callback Method
	 * 
	 * To ensure the user interface is refreshable
	 */
	IUIRefreshable uiPOIList;
	
	private ArrayList<OpenStreetMapViewOverlayItem> items;
	
	/** The map view*/
	private OpenStreetMapView openStreetMapView;
	
	/** The latitude*/
	private double latitude;
	
	/** The longitude */
	private double longitude;
	
	/**
	 * \brief
	 * Constructor to create the Dialog. This dialog isn't
	 * modal, such as any Android dialog.
	 * 
	 * It is necessary that the class which creates this dialog
	 * implements the IUIRefreshable interface
	 * 
	 * @param context   The Dialog Environment
	 * @param poi The POI object
	 * @param openStreetMapView  The map view
	 * @param items 
	 * @param poiID The position index of the POI in the list
	 * @param latitude The latitude of the POI
	 * @param longitude The longitude of the POI
	 * @see IUIRefreshable
	 */
	public NewPOIDialog(Context context, double latitude, double longitude, ArrayList<OpenStreetMapViewOverlayItem> items, OpenStreetMapView openStreetMapView){
		super(context);
		this.context     = context;
		this.latitude = latitude;
		this.longitude = longitude;
		this.items = items;
		this.openStreetMapView = openStreetMapView;
		this.poic    = POIContainer.getInstance(context);
	}

	/**
	 * \brief
	 * Implementierung der Lebenszyklusphase onCreate für den Dialog
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_poi_dialog);
		
		/* Set the title of the dialog box */
		String title = context.getString(R.string.poiNewDialogTitle);
		setTitle(title);
		
		/* Set the text of the edit field */
		EditText subTitle = ((EditText)findViewById(R.id.poiNewDialogNameText));
		
		/* Setup Listener sadly the xml version android:onClick doesn't work */
		((Button)findViewById(R.id.poiDialogButtonAdd)).setOnClickListener(new OnClickPOIAdd());
		
		((Button)findViewById(R.id.poiDialogButtonAbort)).setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) 
			{
				NewPOIDialog.this.cancel();
			}
		});
		
	}

	/**
	 * \brief
	 * Implementation of the necessary onClickListener 
	 */
	private class OnClickPOIAdd implements android.view.View.OnClickListener{
		public void onClick(View v) {
			
			// Get the text from the EditText field
			String newName = ((EditText)findViewById(R.id.poiNewDialogNameText)).getText().toString(); 
				
			try
			{
				// Adding a POI
				poic.addPOI(new POI(latitude, longitude, newName));
				
				OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = new OpenStreetMapViewOverlayItem(newName, "", new GeoPoint(latitude, longitude));
				items.add(openStreetMapViewOverlayItem);
		    	openStreetMapView.invalidate(); //neu zeichnen
				cancel();
			}
			catch (SQLiteConstraintException e) 
			{
				// Setting the error message
				((TextView)findViewById(R.id.poiDialogError))
						.setText(e.getMessage());
			}
			
		}
	}
	
}
