package android.discoveryRallye;

import java.io.Serializable;


public class POI implements Serializable {
	
	private static final long serialVersionUID = 4759185882642148767L;
	private double lat;
	private double lon;
	private String description;
	
	public POI(double lat, double lon, String description) {
		this.lat = lat;
		this.lon = lon;
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLon() {
		return lon;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
}
