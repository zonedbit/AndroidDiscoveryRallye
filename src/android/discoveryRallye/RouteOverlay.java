package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.OpenStreetMapView.OpenStreetMapViewProjection;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

public class RouteOverlay extends OpenStreetMapViewOverlay {

	private final ArrayList<GeoPoint> geoPoints;

	public RouteOverlay(ArrayList<GeoPoint> geoPoints, Context context) 
	{
		super(context);
		this.geoPoints = geoPoints;
	}

	@Override
	protected void onDraw(Canvas canvas, OpenStreetMapView openStreetMapView) 
	{
		OpenStreetMapViewProjection projection = openStreetMapView.getProjection();
	    Path p = new Path();
	    for (int i = 0; i < geoPoints.size(); i++) {
	    if (i == geoPoints.size() - 1) 
	    {
	        break;
	    }
	    Point from = new Point();
	    Point to = new Point();
	    projection.toMapPixels(geoPoints.get(i), from);
	    projection.toMapPixels(geoPoints.get(i + 1), to);
	    p.moveTo(from.x, from.y);
	    p.lineTo(to.x, to.y);
	    }
	    Paint mPaint = new Paint();
	    mPaint.setStyle(Style.FILL);
	    mPaint.setColor(0x00000000);
	    mPaint.setAntiAlias(true);
	    canvas.drawPath(p, mPaint);
	}

	@Override
	protected void onDrawFinished(Canvas arg0, OpenStreetMapView arg1) {
		// TODO Auto-generated method stub
		
	}

}
