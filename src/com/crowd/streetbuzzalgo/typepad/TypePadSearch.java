/**
 * 
 */
package com.crowd.streetbuzzalgo.typepad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class TypePadSearch implements SystemConstants {

	/**
	 * 
	 */
	public TypePadSearch() {
		// TODO Auto-generated constructor stub
	}
	public static List search(List keylist){
		List retList = new ArrayList();
		return retList;
	}
	
	private static List search(String query)throws Exception{
		List list = new ArrayList();
		query = URLEncoder.encode(query,"UTF-8");
		String tumblrurl = typepadapiurl+"?q="+query;
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				tumblrurl).openStream()));
		String inputLine;
		StringBuffer sbfr = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			sbfr.append(inputLine);
		}

		in.close();
	
		return list;
	}
	
	private static List searchnew(String query)throws Exception{
		List list = new ArrayList();
		query = URLEncoder.encode(query,"UTF-8");
		String tumblrurl = typepadapiurl+"?q="+query;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(tumblrurl);
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		StringBuffer sbfr = new StringBuffer();
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
		//System.out.println(sbfr);
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			searchnew("pizza");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
