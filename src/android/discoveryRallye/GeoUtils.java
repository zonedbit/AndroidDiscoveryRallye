package android.discoveryRallye;

import org.andnav.osm.util.GeoPoint;
import android.location.Location;

public class GeoUtils 
{
	public static GeoPoint createGeoPoint(Location retrievedUserLocation)
	{
		return new GeoPoint(retrievedUserLocation.getLatitude(), retrievedUserLocation.getLongitude());
	}
}
