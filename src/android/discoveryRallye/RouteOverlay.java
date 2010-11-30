package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.OpenStreetMapView.OpenStreetMapViewProjection;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	    
	    //ersten Punkt setzen
	    Point startingPoint = new Point();
	    
	    GeoPoint geoPoint = geoPoints.get(0);
	    
	    projection.toMapPixels(geoPoint, startingPoint);
	    p.moveTo(startingPoint.x, startingPoint.y);
	    
	    if(!geoPoints.isEmpty())
	    {
	    	for (int i = 1; i < geoPoints.size() - 1; i++) 
	    	{
	    		Point to = new Point();
	    		projection.toMapPixels(geoPoints.get(i), to);
	    		p.lineTo(to.x, to.y);
	    		p.moveTo(to.x, to.y);
	    	}
	    }
	    
	    Paint polygonPaint = new Paint();
	    polygonPaint.setStrokeWidth(5); 
	    polygonPaint.setColor(Color.BLACK);
	    polygonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    polygonPaint.setAntiAlias(true);
	    canvas.drawPath(p, polygonPaint);
	}

	@Override
	protected void onDrawFinished(Canvas arg0, OpenStreetMapView arg1) {
		// TODO Auto-generated method stub
		
	}

}
