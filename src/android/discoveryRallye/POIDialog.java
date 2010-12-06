package android.discoveryRallye;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class POIDialog extends Dialog {

	private Context ctx;
	
	// TODO Comment me
	public POIDialog(Context ctx){
		super(ctx);
		this.ctx = ctx;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_dialog);
		
		String title = ctx.getString(R.string.poiDialogTitle);
		setTitle(title + " " + "XY");
		
		/* Setup Listener sadly the xml version android:onClick dosen't work*/
		((Button)findViewById(R.id.poiDialogButtonRename))
			.setOnClickListener(new OnClickPOIRename());
		
		((Button)findViewById(R.id.poiDialogButtonGoTo))
			.setOnClickListener(new OnClickPOIGoTo());
		
		((Button)findViewById(R.id.poiDialogButtonDelete))
			.setOnClickListener(new OnClickPOIDelete());

		// TODO Maybe not necessary
		/* is called after cancel() */
		setOnCancelListener(new OnClose());
	}


	
	/*
	 * Implementation of all necessary onClickListener 
	 */
	private class OnClickPOIRename implements android.view.View.OnClickListener{
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIRename()");
			
			// TODO Implement me
			
			// Close the Dialog
			cancel();
		}
	}
	
	private class OnClickPOIGoTo implements android.view.View.OnClickListener{
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIGoTo()");
			
			// TODO Implement me
			
			// Close the Dialog
			cancel();
		}
	}
	
	private class OnClickPOIDelete implements android.view.View.OnClickListener{
		public void onClick(View v) {
			Log.i("DiscoveryRallye","POIDialog::onClickPOIDelete()");
			
			// TODO Implement me
			
			// Close the Dialog
			cancel();
		}
	}

	// TODO Maybe not necessary
	private class OnClose implements OnCancelListener{
		@Override
		public void onCancel(DialogInterface dialog) {
			Log.i("DiscoveryRallye","POIDialog::onCancel(DialogInterface dialog)");
		}
	}
	
	
}
