package android.discoveryRallye;

import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;

/**
 * \brief
 *	Dies ist ein leerer Listener, der für das ItemizedOverlay (Overlay für die POIs) implementiert werden musste. 
 * @param <OpenStreetMapViewOverlayItem>
 */
public class ItemGestureListener<OpenStreetMapViewOverlayItem> implements OnItemGestureListener<OpenStreetMapViewOverlayItem> {

	public ItemGestureListener() {
	}

	public boolean onItemLongPress(int arg0, Object arg1) {
		return false;
	}

	public boolean onItemSingleTapUp(int arg0, Object arg1) {
		return false;
	}
}
