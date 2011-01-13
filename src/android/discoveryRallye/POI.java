package android.discoveryRallye;

import java.io.Serializable;

/**
 *\brief
 *Diese Klasse ist ein Bean, dass serialisiert werden kann. Der POI
 *enthält die Koordinaten und eine Beschreibung als Attribute.
 */
public class POI implements Serializable {
	
	private static final long serialVersionUID = 4759185882642148767L;
	
	/** The latitude of a POI*/
	private double lat;
	/** The longitude of a POI */
	private double lon;
	/** The name of a POI*/
	private String description;
	
	/**
	 *\brief
	 * Konstruktor zur Erzeugung eines POIs
	 * @param lat
	 * @param lon
	 * @param description
	 */
	public POI(double lat, double lon, String description) {
		this.lat = lat;
		this.lon = lon;
		this.description = description;
	}
	/**
	 * \brief
	 * Simpler Setter für die Beschreibung.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * \brief
	 * Simpler Getter für die Beschreibung
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * \brief
	 * Simpler Setter für den Längengrad. 
	 * @param lon
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
	/**
	 * \brief
	 * Simpler Getter für den Längengrad. 
	 */
	public double getLon() {
		return lon;
	}
	/**
	 * \brief
	 * Simpler Setter für den Breitengrad. 
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * \brief
	 * Simpler Getter für den Breitengrad. 
	 */
	public double getLat() {
		return lat;
	}
}
