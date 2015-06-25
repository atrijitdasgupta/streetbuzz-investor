/**
 * 
 */
package com.crowd.streetbuzzalgo.synonym;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @author Atrijit
 *
 */
public class WikiSynonymAPICaller implements SystemConstants{
	
	public static List getSynonymsList(String phrase)throws Exception{
		phrase = phrase.replaceAll(" ", "%252520");
		String url = WIKI_SYNONYM_CORE_URL+phrase;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader(WIKI_HEADER_KEY, WIKI_HEADER_VALUE);
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		StringBuffer sbfr = new StringBuffer();
		if(entity!=null){
			Reader reader = new InputStreamReader(entity.getContent());
            int i;
            char c;
            //StringBuffer sbfr = new StringBuffer();
            while((i=reader.read())!=-1)
            {
               c=(char)i;
               sbfr.append(c);
             
            }
		}
		httpclient.close();
		response.close();
		//parse StringBuffer to get the values out
		String jsonstr = sbfr.toString();
		String[] arr = jsonstr.split(",");
		String val = "";
		List aList = new ArrayList();
		
		for (int i = 0; i < arr.length; i++) {
			String temp = arr[i];

			/*if (temp.indexOf("term") > 0) {
				val = temp.substring(temp.indexOf("term") + "term':'".length(),
						temp.length() - 1);

			}*/
			if (val.indexOf("temp") < 0) {
				if (!aList.contains(val)) {
					val = DataClean.clean(val);
					val = StrUtil.nonNull(val);
					if (!"".equalsIgnoreCase(val)) {
						aList.add(val);

					}

				}
			}

		}
		for (int i = 0; i < aList.size(); i++) {
			String prnt = (String) aList.get(i);
			System.out.println(prnt);
		}
		
		return aList;
	}
		
	public static void getSynonyms(String phrase)throws Exception{
		//phrase = URLEncoder.encode(phrase, "UTF-8");
		phrase = phrase.replaceAll(" ", "%252520");
		String url = WIKI_SYNONYM_CORE_URL+phrase;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader(WIKI_HEADER_KEY, WIKI_HEADER_VALUE);
		//System.out.println(httpget.getURI());
		CloseableHttpResponse response = httpclient.execute(httpget);
		
		
		//System.out.println(response.toString());
	//	System.out.println(response.getStatusLine().toString());
		HttpEntity entity = response.getEntity();
		StringBuffer sbfr = new StringBuffer();
		if (entity != null) {
			  //   System.out.println(EntityUtils.toString(entity));
	            Gson gson = new GsonBuilder().create();
	            ContentType contentType = ContentType.getOrDefault(entity);
	            Charset charset = contentType.getCharset();
	           // Reader reader = new InputStreamReader(entity.getContent(), charset);
	            Reader reader = new InputStreamReader(entity.getContent());
	            int i;
	            char c;
	            //StringBuffer sbfr = new StringBuffer();
	            while((i=reader.read())!=-1)
	            {
	               // int to character
	               c=(char)i;
	               sbfr.append(c);
	               // print char
	              // System.out.println("Character Read: "+c);
	            }
	            System.out.println(sbfr.toString());
	        }
		

		
		try{
			httpclient.close();
			response.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			httpclient.close();
			response.close();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//getSynonymsList("Games & Hobbies");
			getSynonyms("Games");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
