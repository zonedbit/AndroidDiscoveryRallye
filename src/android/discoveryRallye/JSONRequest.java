package android.discoveryRallye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONRequest {

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
	
	public ArrayList<GeoPoint> createRequest(POI myLocation, POI destination)
	{
		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(createURL(myLocation, destination));
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				
				if(entity != null)
				{
					String content = getContent(entity);
					
					JSONObject jsonObject = new JSONObject(content);
					
					JSONArray jsonArray = jsonObject.getJSONArray("coordinates");
					
					
					for (int i = 0; i < jsonArray.length(); i++) 
					{
						JSONArray jsonGeoPoint = jsonArray.getJSONArray(i);
						
						double lon = (Double) jsonGeoPoint.get(0);
						double lat = (Double) jsonGeoPoint.get(1);
						
						GeoPoint geoPoint = new GeoPoint(lat, lon);
						
						geoPoints.add(geoPoint);
					}
				}
			}
			else
			{
				//TODO: Fehlermeldung schmeißen
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return geoPoints;
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
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
		
		StringBuilder sb = new StringBuilder();
		
		String result = "";
		
		while((result = bufferedReader.readLine()) !=null)
		{
			sb.append(result);
		}
		
		return sb.toString();
	}
}
