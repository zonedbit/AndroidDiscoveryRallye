package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The Dialog to interacting with a POI
 * 
 * This class provides the dialog to interacting with
 * one POI form the POI list view.
 * 
 * @author axel
 *
 */
public class POIDialog extends Dialog {

	/** The name of the class; just for debugging */
	public static final String TAG = POIDialog.class.getName();
	
	/** The primary key of the poi */
	private int    	poiID;
	/** The parent of this dialog*/
	private Context context;
	/** reference to the POI container class*/
	POIContainer poic;
	
	/** Refresh the User Interface
	 * 
	 *  To ensure the calling calls implements this interface,
	 *  so that the view can be refresh
	 */
	IUIRefreshable uiPOIList;
	
	/**
	 * Add a route Overlay
	 * 
	 * To ensure the calling class has the method to
	 * add the route overlay to the map
	 */
	IOverlayRouteable route;
	
	/**
	 * Constructor to create the Dialog. This dialog isn't
	 * modal, such as any Android dialog.
	 * 
	 * It is necessary that the class which creates this dialog
	 * implements the IUIRefreshable interface
	 * 
	 * @param ctx   The Dialog Environment
	 * @param ui    Interface for the callback method to refresh the UI
	 * @param route Interface for the callback method to add the route overlay
	 * @param poiID The position index of the POI in the list
	 * @see IUIRefreshable
	 */
	public POIDialog(Context ctx, IUIRefreshable ui, 
					 IOverlayRouteable route, int poiID ){
		super(ctx);
		this.context     = ctx;
		this.poiID 	 = poiID;
		this.poic    = POIContainer.getInstance(ctx);
		this.uiPOIList = ui;
		this.route	   = route;
	}

	/**
	 * Create the Dialog
	 * 
	 * Retrieve the name of the POI and setup the
	 * on click listener for the buttons.
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_dialog);
		
		// The Name of the POI
		String poiName = "Unknown POI";
		if ( poic != null ){
			poiName = poic.getPOI(poiID).getDescription();
		}
		
		/* Set the title of the dialog box */
		String title = context.getString(R.string.poiDialogTitle);
		setTitle(title + " " + poiName );
		
		/* Set the text of the edit field */
		((EditText)findViewById(R.id.poiDialogEditText)).setText(poiName);
		
		/* Setup Listener sadly the xml version android:onClick dosen't work */
		((Button)findViewById(R.id.poiDialogButtonRename))
			.setOnClickListener(new OnClickPOIRename());
		
		((Button)findViewById(R.id.poiDialogButtonGoTo))
			.setOnClickListener(new OnClickPOIGoTo());
		
		((Button)findViewById(R.id.poiDialogButtonDelete))
			.setOnClickListener(new OnClickPOIDelete());
	}


	
	/*
	 * Implementation of all necessary onClickListener 
	 */
	
	/**
	 * On click listener for the OK button
	 */
	private class OnClickPOIRename implements android.view.View.OnClickListener{
		/**
		 * Set the new name of the POI.
		 * 
		 * The name from the text field is used for the new name, if
		 * the user has change the name and this name isn't used.
		 * 
		 * If the new name used a second POI, the user gets a error messages.
		 * 
		 */
		public void onClick(View v) {
			
			// Get the text from the EditText field
			String newName = ((EditText)findViewById(R.id.poiDialogEditText))
								.getText().toString(); 
				
			/* Update the POI in the POIContainer and database
			 * only if the new name different form current name
			 */
			try{
				if ( !poic.getPOI(poiID).getDescription().equals(newName)){
						poic.renamePOI(poiID, newName);
						// Refresh the user interface
						uiPOIList.uiRefresh();
				}
				
				// Close the Dialog
				cancel();
			}catch (SQLiteConstraintException e) {
				// Setting the error message
				((TextView)findViewById(R.id.poiDialogError))
						.setText(e.getMessage());
			}
			
		}
	}
	
	/**
	 * On click listener for the Goto button
	 */
	private class OnClickPOIGoTo implements android.view.View.OnClickListener{
		/**
		 * Call the method addRouteOverlay from the parent class
		 */
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIGoTo()");
			
			POI destination = poic.getPOI(poiID);
			route.addRouteOverlay(destination);
			
			// Close the Dialog
			cancel();
		}
	}
	
	/**
	 * On click listener for the delete button
	 */
	private class OnClickPOIDelete implements android.view.View.OnClickListener{
		/**
		 * Remove a POI from the database
		 */
		public void onClick(View v) {
			
			POI poi = poic.getPOI(poiID);
			
			// Delete the POI from the POIContainer
			poic.removePOI(poi.getDescription(), poiID);
			
			// Close the Dialog
			cancel();
			
			// Refresh the user interface
			uiPOIList.uiRefresh();
			
			//Items holen und das entsprechende Item löschen
			OpenStreetMapViewOverlayItem openStreetMapViewOverlayItem = null;
			ArrayList<OpenStreetMapViewOverlayItem> items = CampusOSM.getItems();
			
			for (OpenStreetMapViewOverlayItem viewOverlayItem : items) {
				if(viewOverlayItem.getTitle().equals(poi.getDescription()))
				{
					openStreetMapViewOverlayItem = viewOverlayItem;
				}
			}
			
			CampusOSM.getItems().remove(openStreetMapViewOverlayItem);
			
			//und neu zeichnen
			CampusOSM.getOpenStreetMapView().invalidate();
			
		}
	}	
}
