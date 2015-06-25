/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import net.elmergarduno.jcalais.CalaisClient;
import net.elmergarduno.jcalais.CalaisConfig;
import net.elmergarduno.jcalais.CalaisObject;
import net.elmergarduno.jcalais.CalaisResponse;
import net.elmergarduno.jcalais.rest.CalaisRestClient;

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
public class InvokeOpenCalaisParser implements SystemConstants {

	/**
	 * 
	 */
	public InvokeOpenCalaisParser() {
		// TODO Auto-generated constructor stub
	}
	public static void jparse(String phrase) throws Exception {
		phrase = URLEncoder.encode(phrase, "UTF-8");
		CalaisClient client = new CalaisRestClient(OPENCALAISSERVICEKEY);
		CalaisConfig config = new CalaisConfig();
		config.set(CalaisConfig.ProcessingParam.CALCULATE_RELEVANCE_SCORE, "true");
		CalaisResponse response = client.analyze(phrase, config);
		for (CalaisObject entity : response.getEntities()) {
		      System.out.println(entity.getField("_type") + ":" 
		                         + entity.getField("name"));
		    }
		for (CalaisObject topic : response.getTopics()) {
		      System.out.println(topic.getField("categoryName"));
		      System.out.println(topic.getField("score"));
		    }
		/*for (CalaisObject relation : response.getRelations()){
		      System.out.println(relation.getField(arg0));
		    }*/
		
	}
	public static void parse(String phrase) throws Exception {
		phrase = URLEncoder.encode(phrase, "UTF-8");
		// //licenseID=url-encoded-string&content=url-encoded-string&paramsXML=url-encoded-string
		StringBuffer params = new StringBuffer();
		params.append("?licenseID=" + OPENCALAISSERVICEKEY + "&content="
				+ phrase);
		String url = OPENCALAISURL + params.toString();
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
			//System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input = "Black Hole";
		try {
			jparse(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
