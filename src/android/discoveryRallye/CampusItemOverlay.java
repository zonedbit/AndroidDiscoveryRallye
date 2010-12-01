package android.discoveryRallye;

import java.util.List;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlayWithFocus;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import android.content.Context;

public class CampusItemOverlay extends OpenStreetMapViewItemizedOverlayWithFocus<OpenStreetMapViewOverlayItem> {

	public CampusItemOverlay(
			Context ctx,
			List<OpenStreetMapViewOverlayItem> items,
			OpenStreetMapViewItemizedOverlay.OnItemGestureListener<OpenStreetMapViewOverlayItem> itemTapListener,
			ResourceProxy resourceProxy) {
		super(ctx, items, itemTapListener, resourceProxy);
	}

	
	
}
