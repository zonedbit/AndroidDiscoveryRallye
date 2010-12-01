package android.discoveryRallye;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Note extends Activity{
	
	/**
	 * The filename to store the user notes
	 */
	private String FILENAME = "private_notes";

	/** 
	 * Create the Note Activity
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
	}
	
	
	/**
	 * 
	 * Save the user notes
	 * 
	 * @see saveNotes()
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		saveNotes();
	}




	/** 
	 * 
	 * Retrieve the information from the file
	 * 
	 * @se restoreNotes()
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Load the notes of the user
		restoreNotes();
	}




	/**
	 * 
	 * Handler to save the user notes
	 * 
	 * @param v The Android View
	 * @see saveNotes()
	 * @throws IOException 
	 */
	public void saveNoteHandler(View v) {
		saveNotes();
	}
	
	/**
	 * Read the saved user notes and write it into the EditText field
	 */
	private void restoreNotes (){
		
		InputStream is = null;
		try {
			is = openFileInput(FILENAME);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			// Reading from the file
			String line = "";
			StringBuilder strB = new StringBuilder();
			while ( (line=br.readLine()) != null){
				strB.append(line);
				strB.append("\n");
				
			}
			
			// Setting the file content to the EditText View
			EditText et = (EditText)findViewById(R.id.noteEditTxt);
			et.setText(strB.toString());
			
			
		} catch (FileNotFoundException e) {
			Log.e("DiscoveryRalley", 
					"Note::restoreNotes () FileNotFoundException" );
		} catch (IOException e) {
			Log.e("DiscoveryRalley", 
					"Note::restoreNotes () IOException by reading the file" );
		} finally {
			if ( is != null ){
				try {
					is.close();
				} catch (IOException e) {
					Log.e("DiscoveryRalley", 
							"Note::restoreNotes () can not close the file input stream" );
				}
			}
		}
	}
	
	
	/**
	 * Save the using notes in the internal storage of this app
	 */
	private void saveNotes(){
		EditText et = (EditText)findViewById(R.id.noteEditTxt);
		String string = et.getText().toString();
		
		OutputStreamWriter out = null;
		try {
			  // open file for writing
			  out = 
				  new OutputStreamWriter(openFileOutput(FILENAME,Context.MODE_PRIVATE));
			  // write the content to the file
			  out.write(string);
			  // close the file
			} catch (java.io.IOException e) {
				Log.e("DiscoveryRalley","Note::saveNoteHandler(View v) IOException");
			} finally {
				if ( out != null ){
					try {
						out.close();
					} catch (IOException e) {
						Log.e("DiscoveryRalley",
								"Note::saveNoteHandler(View v) " +
								"can not close out stream");
					}
				}
			}
	}
}
