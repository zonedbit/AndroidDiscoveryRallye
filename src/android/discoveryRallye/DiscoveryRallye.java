package android.discoveryRallye;

import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class DiscoveryRallye extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost mTabHost = getTabHost();
        
        Intent intent = getIntent();
        
        if(intent != null)
        {
        	Bundle bundle = intent.getExtras();
        	
        	if(bundle != null)
        	{	
        		Intent intentToMap = new Intent(this, CampusOSM.class);
        		intentToMap.putExtras(bundle);
        		
        		//Der Tab soll die Route zum POI darstellen
                mTabHost.addTab(mTabHost.newTabSpec("tabMap")
                		.setIndicator(getString(R.string.nameTabMap))
                		.setContent(intentToMap));
                
                mTabHost.setCurrentTab(0);
        	}
        	else
        	{
                mTabHost.addTab(mTabHost.newTabSpec("tabMap")
                		.setIndicator(getString(R.string.nameTabMap))
                		.setContent(new Intent(this, CampusOSM.class)));
        	}
        	
        	//Diese beiden Tabs bleiben unbeeinflusst vom Intent
        	mTabHost.addTab(mTabHost.newTabSpec("tabPois")
        			.setIndicator(getString(R.string.nameTabPOI))
        			.setContent(new Intent(this, POIList.class)));
        	
        	mTabHost.addTab(mTabHost.newTabSpec("tabNote")
        			.setIndicator(getString(R.string.nameTabNote))
        			.setContent(new Intent(this, Note.class) ));
        	
        	if(bundle == null)
        	{
        		mTabHost.setCurrentTab(0);
        	}
        }
    }
    
	public void onBackPressed() 
	{
		Builder builder = new Builder(this);
		builder.setTitle("Warnung");
		builder.setMessage("Wollen Sie die Anwendung wirklich beenden?");
		builder.setCancelable(false);
		builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) 
			{
				DiscoveryRallye.this.finish();
			}
		});
		
		builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				dialog.cancel();
			}
		});
		
		builder.create();
		builder.show();
	}
}