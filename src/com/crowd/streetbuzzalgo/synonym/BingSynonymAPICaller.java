/**
 * 
 */
package com.crowd.streetbuzzalgo.synonym;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 * 
 */
public class BingSynonymAPICaller implements SystemConstants {

	/**
	 * 
	 */
	public BingSynonymAPICaller() {

	}

	private static void getSynonym(String phrase) throws Exception {
		phrase = URLEncoder.encode(phrase, "UTF-8");
		String url = URIString + phrase;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("account_key", account_key);
		CloseableHttpResponse response = httpclient.execute(httpget);
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

			}
			System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			getSynonym("bicycle");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
