package android.discoveryRallye;

import android.app.TabActivity;
import android.content.Intent;
import android.discoveryRallye.R;
import android.os.Bundle;
import android.widget.TabHost;

public class DiscoveryRallye extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost mTabHost = getTabHost();
        
        
        // Setup tab one
        mTabHost.addTab(mTabHost.newTabSpec("tabMap")
        		.setIndicator(getString(R.string.nameTabMap))
        		.setContent(new Intent(this, CampusOSM.class)));
        
        // Setup tab two
        mTabHost.addTab(mTabHost.newTabSpec("tabPois")
        		.setIndicator(getString(R.string.nameTabPOI))
        		.setContent(R.id.textview2));
        
        // Setup tab three
        mTabHost.addTab(mTabHost.newTabSpec("tabNote")
        		.setIndicator(getString(R.string.nameTabNote))
        		.setContent(new Intent(this, Note.class) ));
        
        mTabHost.setCurrentTab(0);
    }
}