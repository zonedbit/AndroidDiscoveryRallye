package android.discoveryRallye;

public class GeoPoint 
{
	private double lat;
	private double lon;
	
	public GeoPoint(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLon() {
		return lon;
	}
}
