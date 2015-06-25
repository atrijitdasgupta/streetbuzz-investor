/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomy.googlesuggest;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class GoogleSuggest implements SystemConstants {

	/**
	 * 
	 */
	public GoogleSuggest() {
		// TODO Auto-generated constructor stub
	}

	public static List googleSuggest(String phrase) {

		try {
			phrase = URLEncoder.encode(phrase, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = GOOGLE_SUGGEST_URL + phrase;
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
		String xmlStr = sbfr.toString();
	//	System.out.println(xmlStr);
		List aList = new ArrayList();
		try {
			aList = parse(xmlStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(aList);
		return aList;
	}

	private static List parse(String xmlStr) throws Exception {
		List aList = new ArrayList();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlStr));
		Document document = builder.parse(is);
		NodeList nList = document.getElementsByTagName("suggestion");
		for (int i = 0; i < nList.getLength(); i++) {

			NamedNodeMap attributes = nList.item(i).getAttributes();
			for (int a = 0; a < attributes.getLength(); a++) {
				Node theAttribute = attributes.item(a);
				String attrib = StrUtil.nonNull(theAttribute.getNodeValue());
				attrib = attrib.trim();
				//System.out.println(attrib);
				if(!"".equalsIgnoreCase(attrib)){
					aList.add(attrib);
				}
			}
		}
		return aList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		googleSuggest("Hole in the Wall");
		/*
		 * String xmlStr = "<?xml version='1.0'?><toplevel>" + "<CompleteSuggestion><suggestion
		 * data='coffee meets bagel'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee bean'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee cake recipe'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee tables'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee makers'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee enema'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffeescript'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee shops near me'/></CompleteSuggestion>" + "<CompleteSuggestion><suggestion
		 * data='coffee shop'/></CompleteSuggestion></toplevel>"; try {
		 * parse(xmlStr); } catch (Exception e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

	}

}
