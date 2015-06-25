/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;

/**
 * @author Atrijit
 *
 */
public class GeoCodingReverseLookupUtils implements SystemConstants, Constants{
	
	/**
	 * 
	 */
	public GeoCodingReverseLookupUtils() {
		// TODO Auto-generated constructor stub
	}
	public static Map reverseLookup(String latitude, String longitude)throws Exception{
		StringBuffer params = new StringBuffer();
		String latlng = latitude+","+longitude;
		params.append("key="+GOOGLE_GEOCOING_API_KEY);
		params.append("&latlng="+latlng);
		String url = GEOCODING_URL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpget);
		StringBuffer sbfr = new StringBuffer();
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Reader reader = new InputStreamReader(entity.getContent());
			int i;
			char c;
			
			while ((i = reader.read()) != -1) {
				// int to character
				c = (char) i;
				sbfr.append(c);
				// print char
				// System.out.println("Character Read: "+c);
			}
			//System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
		if(sbfr.length()>0){
			Map map =  extractLocations(sbfr.toString());
			System.out.println(map);
			return map;
		}else{
			return new HashMap();
		}
		
	}
	
	private static Map extractLocations(String input){
		System.out.println(input);
		String start = REVGEOSTARTTAG;
		String end = REVGEOENDTAG;
		String temp = input.substring((input.indexOf(start)+start.length()),input.indexOf(end));
		//System.out.println("temp :"+temp);
		List tempList = new ArrayList();
		tempList.add(temp);
		/*String [] locations = temp.split(",");
		List tempList = new ArrayList();
		for(int i=0;i<locations.length;i++){
			String key = locations[i];
			key = key.trim();
			
			StanfordNerVO snvo = ProcessingUtils.getStanfordNamedEntities(key);
			List snkeylist = snvo.getLocation();
			
			String snkey = "";
			if(snkeylist!=null && snkeylist.size()>0){
				snkey = StrUtil.nonNull((String)snkeylist.get(0));
			}
			if(!"".equalsIgnoreCase(snkey)){
				System.out.println("snkey: "+snkey);
				tempList.add(snkey);
			}
			
		}*/
		Map map = new HashMap();
		int size = tempList.size();
		if(tempList!=null && size>0){
			if(tempList.size()==1){
				String temp0 = (String)tempList.get(0);
				map.put(COUNTRY,"");
				map.put(CITY, temp);
			}else if(size>1){
				String temp1 = (String)tempList.get(0);
				String temp2 = (String)tempList.get(size-1);
				map.put(CITY, temp1);
				map.put(COUNTRY,temp2);
			}
		}
		return map;
	}
	public static final String distance(String mylat, String mylon, String yourlat, String yourlon){
		Double mylatd = new Double(0.0);
		Double mylond = new Double(0.0);
		Double yourlatd = new Double(0.0);
		Double yourlond = new Double(0.0);
		
		if(!"".equalsIgnoreCase(mylat)){
			mylatd = new Double(mylat);
		}
		if(!"".equalsIgnoreCase(mylon)){
			mylond = new Double(mylon);
		}
		if(!"".equalsIgnoreCase(yourlat)){
			yourlatd = new Double(yourlat);
		}
		if(!"".equalsIgnoreCase(yourlon)){
			yourlond = new Double(yourlon);
		}
		double distance = distance(mylatd.doubleValue(),mylond.doubleValue(),yourlatd.doubleValue(),yourlond.doubleValue(),"K");
		Double distanced = new Double(distance);
		Integer distanceint = new Integer(distanced.intValue());
		String distancestr = distanceint.toString();
		return distancestr;
	}
	
	private static final double distance(double lat1, double lon1, double lat2,
			double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit.equalsIgnoreCase("K")) {
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")) {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	private static final double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static final double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(distance("13.0001173","77.628918", "12.9602241","77.6363832"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
