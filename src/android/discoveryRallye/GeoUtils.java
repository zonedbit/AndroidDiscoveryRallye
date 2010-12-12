package android.discoveryRallye;

import org.andnav.osm.util.GeoPoint;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GeoUtils 
{
	private static Location location;
	private static LocationManager locationManager;
	
	public static void activateLocationListener(Context context) 
	{
		if(locationManager  == null)
		{
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		
		locationManager.requestLocationUpdates(getBestProvider(locationManager), 1000, 100, new LocationListener() 
		{

			public void onLocationChanged(Location location) 
			{
				GeoUtils.location = location;
			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static Location retrieveLastKnownLocation(Context context)
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.getLastKnownLocation(getBestProvider(locationManager));
	}
	
	public static GeoPoint createGeoPoint(Location retrievedUserLocation)
	{
		return new GeoPoint(retrievedUserLocation.getLatitude(), retrievedUserLocation.getLongitude());
	}
	
	private static String getBestProvider(LocationManager locationManager)
	{
		if(locationManager != null)
		{
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			
			return locationManager.getBestProvider(criteria, true);
		}
		
		//Default
		return LocationManager.GPS_PROVIDER;
	}

	public static Location getLocation() {
		return location;
	}
	
	
}
