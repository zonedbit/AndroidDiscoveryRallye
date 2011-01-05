package android.discoveryRallye;

import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.OpenStreetMapView.OpenStreetMapViewProjection;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

public class RouteOverlay extends OpenStreetMapViewOverlay {

	private ArrayList<GeoPoint> geoPoints;
	private OpenStreetMapView openStreetMapView = null;
	private final Activity activity;
	private final int color;
	
	public  void setGeoPoints(ArrayList<GeoPoint> geoPoints)
	{
		this.geoPoints = geoPoints;
	}

	public RouteOverlay(ArrayList<GeoPoint> geoPoints, OpenStreetMapView openStreetMapView, Activity activity, int color) 
	{
		super(activity);
		this.geoPoints = geoPoints;
		this.openStreetMapView  = openStreetMapView;
		this.activity = activity;
		this.color = color;
	}
	
	@Override
	protected void onDraw(Canvas canvas, OpenStreetMapView openStreetMapView) 
	{
		OpenStreetMapViewProjection projection = this.openStreetMapView.getProjection();
	    Path p = new Path();
	    
	    //ersten Punkt setzen
	    Point startingPoint = new Point();
	    if(!geoPoints.isEmpty())
	    {
	    	GeoPoint geoPoint = geoPoints.get(0);
	    	projection.toMapPixels(geoPoint, startingPoint);
		    p.moveTo(startingPoint.x, startingPoint.y);
	    
	    	for (int i = 1; i < geoPoints.size() - 1; i++) 
	    	{
	    		Point to = new Point();
	    		projection.toMapPixels(geoPoints.get(i), to);
	    		p.lineTo(to.x, to.y);
	    		p.moveTo(to.x, to.y);
	    	}
	    }
	    else
	    {
	    	Builder builder = new Builder(activity);
			builder.setTitle("Probleme beim Darstellen der Route");
			builder.setMessage("Die Route konnte nicht dargestellt werden");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.cancel();
				}
			});
			
			builder.create();
			builder.show();
	    }
	    
	    Paint polygonPaint = new Paint();
	    polygonPaint.setStrokeWidth(3); 
	    polygonPaint.setColor(color);
	    polygonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    polygonPaint.setAntiAlias(true);
	    canvas.drawPath(p, polygonPaint);
	}

	@Override
	protected void onDrawFinished(Canvas arg0, OpenStreetMapView arg1) {
		// TODO Auto-generated method stub
		
	}
}
