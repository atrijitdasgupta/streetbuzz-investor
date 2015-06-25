/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomy.freebase;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Atrijit
 *
 */
public class Freebase implements SystemConstants {

	/**
	 * 
	 */
	public Freebase() {
		
	}
	public static List getList(String query)throws Exception{
		List aList = new ArrayList();
		HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
	      JSONParser parser = new JSONParser();
	      GenericUrl url = new GenericUrl(FREEBASE_GENERIC_URL);
	      url.put("query", query);
	      url.put("limit", "25");
	      url.put("indent", "true");
	      url.put("key", FREEBASE_API_KEY);
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();
	      JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
	      JSONArray results = (JSONArray)response.get("result");
	      for (Object result : results) {
		        String temp = JsonPath.read(result,"$.name").toString();
		        if(!aList.contains(temp)){
		        	System.out.println(temp);
		        	aList.add(temp);
		        }
		      }
	     System.out.println(aList);
		return aList;
	}
	public void search(String query)throws Exception{
		HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
	      JSONParser parser = new JSONParser();
	      GenericUrl url = new GenericUrl(FREEBASE_GENERIC_URL);
	      url.put("query", query);
	      url.put("limit", "25");
	      url.put("indent", "true");
	      url.put("key", FREEBASE_API_KEY);
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();
	      JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
	      JSONArray results = (JSONArray)response.get("result");
	      for (Object result : results) {
	        System.out.println(JsonPath.read(result,"$.name").toString());
	      }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			getList("It happened one night");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
