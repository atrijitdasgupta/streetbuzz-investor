/**
 * 
 */
package com.crowd.streetbuzzalgo.faroosearch;

import java.io.InputStreamReader;
import java.io.Reader;
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
public class SearchFaroo implements SystemConstants{

	/**
	 * 
	 */
	public SearchFaroo() {
		// TODO Auto-generated constructor stub
	}
	public static List search(String query) throws Exception {
		List searchList = new ArrayList();
		String address = FAROO_SEARCH_URL+"&key="+FAROO_SEARCH_KEY;
		address = address.replaceAll("<i>", "10");
		query = URLEncoder.encode(query,CHARSET);
		address = address.replaceAll("<q>", query);
		System.out.println(address);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(address);
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Reader reader = new InputStreamReader(entity.getContent());
			int i;
			char c;
			StringBuffer sbfr = new StringBuffer();
			while ((i = reader.read()) != -1) {
				// int to character
				c = (char) i;
				sbfr.append(c);
				// print char
				// System.out.println("Character Read: "+c);
			}
			System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
		return searchList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			search("Narendra Modi");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
