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

public class NewPOIDialog extends Dialog {

	private Context context;
	POIContainer poic;
	IUIRefreshable uiPOIList;
	private ArrayList<OpenStreetMapViewOverlayItem> items;
	private OpenStreetMapView openStreetMapView;
	private double latitude;
	private double longitude;
	
	/**
	 * Constructor to create the Dialog. This dialog isn't
	 * modal, such as any Android dialog.
	 * 
	 * It is necessary that the class which creates this dialog
	 * implements the IUIRefreshable interface
	 * 
	 * @param context   The Dialog Environment
	 * @param poi 
	 * @param openStreetMapView 
	 * @param items 
	 * @param poiID The position index of the POI in the list
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

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_poi_dialog);
		
		/* Set the title of the dialog box */
		String title = context.getString(R.string.poiDialogTitle);
		setTitle(title);
		
		/* Set the text of the edit field */
		((EditText)findViewById(R.id.poiDialogEditText)).setText("new POI");
		
		/* Setup Listener sadly the xml version android:onClick doesn't work */
		((Button)findViewById(R.id.poiDialogButtonRename)).setOnClickListener(new OnClickPOIAdd());
		
	}

	/*
	 * Implementation of the necessary onClickListener 
	 */
	private class OnClickPOIAdd implements android.view.View.OnClickListener{
		public void onClick(View v) {
			
			// Get the text from the EditText field
			String newName = ((EditText)findViewById(R.id.poiDialogEditText)).getText().toString(); 
				
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
