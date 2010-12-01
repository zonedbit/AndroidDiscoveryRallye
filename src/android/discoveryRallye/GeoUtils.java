package android.discoveryRallye;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class GeoUtils 
{
	public static Location retrieveLocation(Context context) 
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.getLastKnownLocation(getBestProvider(locationManager));
	}
	
	private static String getBestProvider(LocationManager locationManager)
	{
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		return locationManager.getBestProvider(criteria, true);
	}
}
