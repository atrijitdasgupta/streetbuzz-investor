/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

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
public class InvokeRPParser implements SystemConstants {

	/**
	 * 
	 */
	public InvokeRPParser() {
		// TODO Auto-generated constructor stub
	}
	
	private static void parseRankedKeywords(String phrase)throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		//params.append("&knowledgeGraph=1");
		String url = RPRANKEDKEYWORDSURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
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
				// print char
				// System.out.println("Character Read: "+c);
			}
		//	System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
	}
	
	private static void parseEntities(String phrase)throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		params.append("&knowledgeGraph=1");
		params.append("&structuredEntities=1");
		params.append("&disambiguate=1");
		params.append("&linkedData=1");
		params.append("&coreference=1");
		
		String url = RPRANKEDENTITIESURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
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
				// print char
				// System.out.println("Character Read: "+c);
			}
			System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
	}
	
	private static void parseRelations(String phrase)throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		params.append("&knowledgeGraph=1");
		params.append("&entities=1");
		//params.append("&keywords=1");
		//params.append("&linkedData=1");
		//params.append("&coreference=1");
		
		String url = RPRELATIONSURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
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
				// print char
				// System.out.println("Character Read: "+c);
			}
		//	System.out.println(sbfr.toString());
		}
		
	}
	public static void parseSentiment(String phrase) throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		
		String url = RPSENTIMENTURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
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
				// print char
				// System.out.println("Character Read: "+c);
			}
	//	System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
	}
	public static void parseTargetedSentiment(String phrase, String target)throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		params.append("&target="+target);
				
		String url = RPTARGETEDSENTIMENTURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
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
				// print char
				// System.out.println("Character Read: "+c);
			}
		//	System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String input = "Where do I get to see elephant racing in Mysore?";
		String input = "Other movies like It happened One Night";
		//input = "Where do I get to see elephant racing in Mysore? I am told I am better off in Kerala.";
		String target = "BJP";
		try {
			parseEntities(input);
			//parseRelations(input);
			//parseTargetedSentiment (input, target);
			//parseRankedKeywords(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
