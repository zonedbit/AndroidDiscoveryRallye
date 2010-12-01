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
import android.graphics.Path;
import android.graphics.Point;

public class RouteOverlay extends OpenStreetMapViewOverlay {

	private ArrayList<GeoPoint> geoPoints;
	private OpenStreetMapView openStreetMapView = null;

	public RouteOverlay(ArrayList<GeoPoint> geoPoints, Context context, OpenStreetMapView openStreetMapView) 
	{
		super(context);
		this.geoPoints = geoPoints;
		this.openStreetMapView  = openStreetMapView;
	}

	@Override
	protected void onDraw(Canvas canvas, OpenStreetMapView openStreetMapView) 
	{
		OpenStreetMapViewProjection projection = this.openStreetMapView.getProjection();
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
	    polygonPaint.setStrokeWidth(3); 
	    polygonPaint.setColor(Color.RED);
	    polygonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    polygonPaint.setAntiAlias(true);
	    canvas.drawPath(p, polygonPaint);
	}

	@Override
	protected void onDrawFinished(Canvas arg0, OpenStreetMapView arg1) {
		// TODO Auto-generated method stub
		
	}

}
