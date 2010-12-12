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

public class POIDialog extends Dialog {

	public static final String TAG = POIDialog.class.getName();
	private int    	poiID;
	private Context context;
	POIContainer poic;
	IUIRefreshable uiPOIList;
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

	/* (non-Javadoc)
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
	private class OnClickPOIRename implements android.view.View.OnClickListener{
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
	
	private class OnClickPOIGoTo implements android.view.View.OnClickListener{
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIGoTo()");
			
			POI destination = poic.getPOI(poiID);
			route.addRouteOverlay(destination);
			
			// Close the Dialog
			cancel();
		}
	}
	
	private class OnClickPOIDelete implements android.view.View.OnClickListener{
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
