package android.discoveryRallye;

/**
 * Interface to define a callback method
 * 
 * @author axel
 *
 */
public interface IOverlayRouteable {
	/** 
	 * To ensure a class can add a Overlay to the map view
	 * @param destination The destination POI object
	 */
	public void addRouteOverlay(POI destination);

}
