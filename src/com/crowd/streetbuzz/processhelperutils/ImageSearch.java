/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.Permission;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.google.gdata.client.Query;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


/**
 * @author Atrijit
 * 
 */
public class ImageSearch implements SystemConstants, Constants {

	/**
	 * 
	 */
	public ImageSearch() {
		// TODO Auto-generated constructor stub
	}

	public static void FlickrSearch(String searchterms) {
		Flickr f = null;
		try {
			f = new Flickr(FLICKR_KEY, FLICKR_SECRET, new REST());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		REST rest;
		RequestContext requestContext = RequestContext.getRequestContext();
		Auth auth = new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(FLICKR_TOKEN);
		requestContext.setAuth(auth);
		Flickr.debugRequest = false;
		Flickr.debugStream = false;
	}

	public static void picasaSearch(String searchterms) {
		PicasawebService myService = new PicasawebService(
				"exampleCo-exampleApp-1");
		try {
			myService.setUserCredentials("atrijitdasgupta@gmail.com",
					"lkpg363jkw22");
		} catch (AuthenticationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URL baseSearchUrl = null;
		try {
			baseSearchUrl = new URL(
					"https://picasaweb.google.com/data/feed/api/all");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query myQuery = new Query(baseSearchUrl);
		myQuery.setStringCustomParameter("kind", "photo");
		myQuery.setMaxResults(10);
		myQuery.setFullTextQuery(searchterms);
		AlbumFeed searchResultsFeed = null;
		try {
			searchResultsFeed = myService.query(myQuery, AlbumFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (PhotoEntry photo : searchResultsFeed.getPhotoEntries()) {
			System.out.println(photo.getTitle().getPlainText());
		}
	}

	public static List googleImageSearch(List searchtermList) {
		List finalList = new ArrayList();
		List imagesList = new ArrayList();
		for  (int i=0;i<searchtermList.size();i++){
			String searchterm = (String)searchtermList.get(i);
			imagesList = googleImageSearch(searchterm);
		}
		for (int i=0;i<imagesList.size();i++){
			String temp = (String)imagesList.get(i);
			if(!finalList.contains(temp)){
				System.out.println("temp:"+temp);
				finalList.add(temp);
			}
		}
		return finalList;
	}
	
	
	public static List googleImageSearch(String searchterms) {
		List imagesList = new ArrayList();
		try {
			searchterms = URLEncoder.encode(searchterms, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URL url = null;
		try {
			url = new URL(
					"https://ajax.googleapis.com/ajax/services/search/images?"
							+ "v=1.0&q=" + searchterms
							+ "&userip=223.227.105.118");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.addRequestProperty("Referer", "http://www.street.buzz");
		String line = "";
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject json = new JSONObject(builder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = builder.toString();
		String[] tempArr = temp.split("unescapedUrl");
		if(tempArr!=null){
			for (int i=0;i<tempArr.length;i++){
				String ctemp = tempArr[i];
				System.out.println(ctemp);
				String ptemp = ctemp.substring((ctemp.indexOf(":")+2),(ctemp.indexOf(",")-1));
				System.out.println(ptemp);
				imagesList.add(ptemp);
			}
			
		}
		return imagesList;
		//System.out.println(builder.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("sunil manohar gavaskar");
		//list.add("Diego Maradona");
		ImageSearch.googleImageSearch("Sunil Manohar Gavaskar");
	}

}
