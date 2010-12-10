package android.discoveryRallye;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class POIDialog extends Dialog {

	private int    	poiID;
	private Context ctx;
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
		this.ctx     = ctx;
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
		String title = ctx.getString(R.string.poiDialogTitle);
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
				
			// Update the Point in the POIContainer
			poic.renamePOI(poiID, newName);
			
			// Close the Dialog
			cancel();
			
			// Refresh the user interface
			uiPOIList.uiRefresh();
		}
	}
	
	private class OnClickPOIGoTo implements android.view.View.OnClickListener{
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIGoTo()");
			
			// TODO Implement me
			POI destination = poic.getPOI(poiID);
			route.addRouteOverlay(destination);
			
			// Close the Dialog
			cancel();
		}
	}
	
	private class OnClickPOIDelete implements android.view.View.OnClickListener{
		public void onClick(View v) {
			
			// Delete the POI from the POIContainer
			poic.removePOI(poic.getPOI(poiID).getDescription(), poiID);
			
			// Close the Dialog
			cancel();
			
			// Refresh the user interface
			uiPOIList.uiRefresh();
		}
	}	
}
