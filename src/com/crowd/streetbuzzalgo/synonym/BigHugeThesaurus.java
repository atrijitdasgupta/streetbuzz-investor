/**
 * 
 */
package com.crowd.streetbuzzalgo.synonym;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class BigHugeThesaurus implements SystemConstants {

	/**
	 * 
	 */
	public BigHugeThesaurus() {
		// TODO Auto-generated constructor stub
	}
	
	public static List getThesaurus(String phrase){
		
		try {
			phrase = URLEncoder.encode(phrase, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = BHT_URL+phrase+"/xml";
		System.out.println(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpEntity entity = response.getEntity();
		StringBuffer sbfr = new StringBuffer();
		if (entity != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(entity.getContent());
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int i;
			char c;
			// StringBuffer sbfr = new StringBuffer();
			try {
				while ((i = reader.read()) != -1) {
					c = (char) i;
					sbfr.append(c);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			httpclient.close();
			response.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String xmlStr = StrUtil.nonNull(sbfr.toString());
		System.out.println(xmlStr);
		List aList = new ArrayList();
		try {
			if(!"".equalsIgnoreCase(xmlStr)){
				aList = parse(xmlStr);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aList;
	}
	private static List parse(String xmlStr)throws Exception{
		List aList = new ArrayList();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlStr));
		Document document = builder.parse(is);
		NodeList nList = document.getElementsByTagName("w");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = (Node)nList.item(i);
			String val = StrUtil.nonNull(node.getTextContent());
		//	System.out.println(val);
			aList.add(val);
		}
		return aList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = getThesaurus("medicine");
		StringBuffer sbfr = new StringBuffer();
		for (int i=0;i<list.size();i++){
			String temp = (String)list.get(i);
			sbfr.append(temp+",");
		}
		System.out.println(sbfr.toString());

	}

}
