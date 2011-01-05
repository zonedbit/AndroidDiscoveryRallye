package android.discoveryRallye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.andnav.osm.contributor.util.Util;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.EventLogTags.Description;
import android.util.Log;


public class JSONRequest extends AsyncTask<ArrayList<POI>, Void , ArrayList<GeoPoint>> {

	//Quelle: http://wiki.openstreetmap.org/wiki/YOURS#Routing_API
	private static final String BASEURL = "http://www.yournavigation.org/api/1.0/gosmore.php?";
	private static final String FORMAT = "format";
	private static final String FLAT = "flat";
	private static final String FLON = "flon";
	private static final String TLAT = "tlat";
	private static final String TLON = "tlon";
	private static final String TYPE_OF_TRANSPORT = "v";
	private static final String FAST = "1";
	private static final String LAYER = "mapnik";
	private static final String GEOJSON = "geojson";
	private static final String FOOT = "foot";
	private static final Object MAPNIK = "mapnik";
	private ProgressDialog dialog;
	private Activity activity;
	private POI destination;
	
	public JSONRequest(Activity activity, POI destination)
	{
		this.activity = activity;
		this.destination = destination;
	}
	
	/*
	 * http://www.yournavigation.org/api/1.0/gosmore.php?format=geojson&flat=52.215676&flon=5.963946&tlat=52.2573&tlon=6.1799&v=foot&fast=1&layer=mapnik
	 */
	private String createURL(POI myLocation, POI destination) 
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(BASEURL);
		sb.append(FORMAT);
		sb.append("=");
		sb.append(GEOJSON);
		sb.append("&");
		sb.append(FLAT);
		sb.append("=");
		sb.append(myLocation.getLat());
		sb.append("&");
		sb.append(FLON);
		sb.append("=");
		sb.append(myLocation.getLon());
		sb.append("&");
		sb.append(TLAT);
		sb.append("=");
		sb.append(destination.getLat());
		sb.append("&");
		sb.append(TLON);
		sb.append("=");
		sb.append(destination.getLon());
		sb.append("&");
		sb.append(TYPE_OF_TRANSPORT);
		sb.append("=");
		sb.append(FOOT);
		sb.append("&");
		sb.append(FAST);
		sb.append("=");
		sb.append("1");
		sb.append("&");
		sb.append(LAYER);
		sb.append("=");
		sb.append(MAPNIK);
		
		return sb.toString();
	}

	private String getContent(HttpEntity entity) throws IOException {
		InputStream content = entity.getContent();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content), 8);
		
		StringBuilder sb = new StringBuilder();
		
		String result = "";
		
		while((result = bufferedReader.readLine()) !=null)
		{
			sb.append(result);
		}
		
		return sb.toString();
	}
	
	@Override
	protected void onPreExecute() 
	{
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Laden der Route...");
		dialog.show();
	}

	protected ArrayList<GeoPoint> doInBackground(ArrayList<POI>... pois) 
	{
		Log.d(JSONRequest.class.getName(), "Abfrage des Webservices startet...");
		
		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(createURL(pois[0].get(0), pois[0].get(1)));
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			Log.d(JSONRequest.class.getName(), "Ausfuehrung der Antwort");
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				Log.d(JSONRequest.class.getName(), "StatusCode = 200");
				
				HttpEntity entity = response.getEntity();
				
				if(entity != null)
				{
					Log.d(JSONRequest.class.getName(), "Rueckgabewert vorhanden");
					
					String content = getContent(entity);
					
					if(!content.contains("busy"))
					{
						Log.d(JSONRequest.class.getName(), "Server is not busy");
						
						JSONObject jsonObject = new JSONObject(content);
						
						JSONArray jsonArray = jsonObject.getJSONArray("coordinates");
						
						for (int i = 0; i < jsonArray.length() - 1; i++) 
						{
							JSONArray jsonGeoPoint = jsonArray.getJSONArray(i);
							
							double lon = (Double) jsonGeoPoint.get(0);
							double lat = (Double) jsonGeoPoint.get(1);
							
							GeoPoint geoPoint = new GeoPoint(lat, lon);
							
							geoPoints.add(geoPoint);
						}
					}
				}
			}
		
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d(JSONRequest.class.getName(), e.getMessage());
		}
		
		Log.d(JSONRequest.class.getName(), "Abfrage des Webservices abgeschlossen");
		
		return geoPoints;
	}
	
	@Override
	protected void onPostExecute(ArrayList<GeoPoint> result) 
	{
		if(result.size() > 0)
		{
			//sonst ist es die Route, die auf der Karte neu gezeichnet wird
			if(activity instanceof POIList)
			{
				Intent intent = new Intent(activity, DiscoveryRallye.class);
				Bundle bundle = new Bundle();
				
				bundle.putParcelableArrayList("pois", result);
				bundle.putSerializable("destination", destination);
				intent.putExtras(bundle);
				dialog.dismiss();
				activity.startActivity(intent);
				activity.finish();
			}
			else if(activity instanceof CampusOSM)
			{
				dialog.dismiss();
				
				OpenStreetMapViewOverlay overlayToRemove = null;
				
				//das vorherige Overlay muss entfernt werden
				for(OpenStreetMapViewOverlay overlay : CampusOSM.getOpenStreetMapView().getOverlays())
				{
					if(overlay instanceof RouteOverlay)
					{
						overlayToRemove = overlay;
					}
				}
				
				if(overlayToRemove != null)
				{
					CampusOSM.getOpenStreetMapView().getOverlays().remove(overlayToRemove);
				}
				
				CampusOSM.getOpenStreetMapView().getOverlays().add(new RouteOverlay(result, CampusOSM.getOpenStreetMapView(), activity, Color.YELLOW));
				
				CampusOSM.getOpenStreetMapView().getController().setCenter(result.get(0));
				CampusOSM.getOpenStreetMapView().invalidate();
			}
		}
		else
		{
			{
				dialog.dismiss();
				
				Builder builder = new Builder(activity);
				builder.setTitle("Probleme beim Laden der Route");
				builder.setMessage("Routeninformationen konnten nicht heruntergeladen werden - Service nicht erreichbar");
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
		}
		
	}

	@SuppressWarnings("unchecked")
	public void calculateRoute() {
		
		if(destination != null)
		{
			ArrayList<POI> pois = new ArrayList<POI>();
			
			POI myLocation = null;
			
			GeoPoint geoPoint = CampusOSM.getMyLocationOverlay().getMyLocation();
			
			if(geoPoint != null)
			{
				Location userLocation = new Location("my Location");
				
				userLocation.setLatitude(geoPoint.getLatitudeE6() / 1E6);
				userLocation.setLongitude(geoPoint.getLongitudeE6() / 1E6);
				
				if(userLocation != null)
				{
					myLocation  = new POI(userLocation.getLatitude(), userLocation.getLongitude(), "My Location");
				}
				
				pois.add(myLocation);
				pois.add(destination);
				
				this.execute(pois);
			}
			else
			{
				Builder builder = new Builder(activity);
				builder.setTitle("GPS-Signal");
				builder.setMessage("Es ist noch kein GPS-Signal aufrufbar");
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
		}
		else
		{
			Builder builder = new Builder(activity);
			builder.setTitle("Keine Route wurde ausgewählt");
			builder.setMessage("Da keine Route ausgewählt wurde, kann auch keine neu gezeichnet werden.");
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
	}
}
