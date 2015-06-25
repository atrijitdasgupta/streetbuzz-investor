/**
 * Conf.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import es.daedalus.textalytics.sempub.SempubClient.CheckSpacing;
import es.daedalus.textalytics.sempub.SempubClient.FilterData;
import es.daedalus.textalytics.sempub.SempubClient.Format;
import es.daedalus.textalytics.sempub.SempubClient.Mode;
import es.daedalus.textalytics.sempub.SempubClient.Params;
import es.daedalus.textalytics.sempub.SempubClient.Type;
import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.domain.Document;
import es.daedalus.textalytics.sempub.domain.Document.Language;
import es.daedalus.textalytics.sempub.domain.Resource;

/**
 * Helper class for setting request parameters corresponding to each of the different
 * possible operations of the API.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class Conf {

	public static String setGetParams(String key, Format input, Format output, String q, Language lang) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair(Params.key.name(), key));
		params.add(new BasicNameValuePair(Params.input.name(), input.name()));
		params.add(new BasicNameValuePair(Params.output.name(), output.name()));
				
		if(q != null && !q.isEmpty()) params.add(new BasicNameValuePair(Params.query.name(), q));
		if(lang != null) params.add(new BasicNameValuePair(Params.lang.name(), lang.name()));
    	
		params.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		
    	String query = URLEncodedUtils.format(params, "utf-8");
    	
    	return query;
    	
    }
	
	public static String setGetParams(String key, Format input, Format output) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair(Params.key.name(), key));
		params.add(new BasicNameValuePair(Params.input.name(), input.name()));
		params.add(new BasicNameValuePair(Params.output.name(), output.name()));
		params.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		
		String query = URLEncodedUtils.format(params, "utf-8");
    	
    	return query;
    	
    }
	
	public static void setPostParams(HttpPost post, Resource resource, Type r, String key, Format input, Format output) throws TextalyticsClientException {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair(Params.key.name(), key));
		params.add(new BasicNameValuePair(Params.input.name(), input.name()));
		params.add(new BasicNameValuePair(Params.output.name(), output.name()));
		params.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		if(resource != null) params.add(new BasicNameValuePair(r.name(), resource.toJsonString()));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new TextalyticsClientException(500, "Problem encoding HTTP POST method parameters.\n", e);
		}
    }
	
	public static void setPutParams(HttpPut put, Resource resource, Type r, String key, Format input, Format output) throws TextalyticsClientException {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair(Params.key.name(), key));
		params.add(new BasicNameValuePair(Params.input.name(), input.name()));
		params.add(new BasicNameValuePair(Params.output.name(), output.name()));
		params.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		if(resource != null) params.add(new BasicNameValuePair(r.name(), resource.toJsonString()));
		
		try {
			put.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new TextalyticsClientException(500, "Problem encoding HTTP PUT method parameters.\n", e);
		}
    }
	
	public static String setDeleteParams(String key, Format input, Format output) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	params.add(new BasicNameValuePair(Params.key.name(), key));
		params.add(new BasicNameValuePair(Params.input.name(), input.name()));
		params.add(new BasicNameValuePair(Params.output.name(), output.name()));
		params.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		
		String query = URLEncodedUtils.format(params, "utf-8");
		
		return query;
    }
	
	public static void setPostParams(HttpPost post, Document document, String key, Format input, 
			Format output, String fields, String dictionary, String models,
			FilterData filterData) throws TextalyticsClientException {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(Params.key.name(), key));
		nvps.add(new BasicNameValuePair(Params.input.name(), input.name()));
		nvps.add(new BasicNameValuePair(Params.output.name(), output.name()));
		
		if (fields != null && !fields.isEmpty())
			nvps.add(new BasicNameValuePair(Params.fields.name(), fields));
				
		String s = document.toJsonString();
		nvps.add(new BasicNameValuePair(Params.doc.name(), s ));
		
		if (dictionary != null && !dictionary.isEmpty())
			nvps.add(new BasicNameValuePair(Params.dictionary.name(), dictionary));
		
		if (models != null && !models.isEmpty())
			nvps.add(new BasicNameValuePair(Params.model.name(), models));
		
		if (filterData != null)
			nvps.add(new BasicNameValuePair(Params.filter_data.name(), filterData.name()));
		
		nvps.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new TextalyticsClientException(500, "Problem encoding HTTP POST method parameters.\n", e);
		}
	}
	
	public static void setPostParams(HttpPost post, Document document, String key, Format input, Format output, 
			int docOffset, int groupErrors, Mode mode, CheckSpacing checkSpacing, 
			String dictionary) throws TextalyticsClientException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(Params.key.name(), key));
		nvps.add(new BasicNameValuePair(Params.input.name(), input.name()));
		nvps.add(new BasicNameValuePair(Params.output.name(), output.name()));
		
		String s = document.toJsonString();
		nvps.add(new BasicNameValuePair(Params.doc.name(), s ));
		
		nvps.add(new BasicNameValuePair(Params.doc_offset.name(), String.valueOf(docOffset)));
		
		nvps.add(new BasicNameValuePair(Params.group_errors.name(), String.valueOf(groupErrors)));
		
		if(mode != null)
			nvps.add(new BasicNameValuePair(Params.mode.name(), mode.name()));
		
		if(checkSpacing != null)
			nvps.add(new BasicNameValuePair(Params.check_spacing.name(), checkSpacing.name()));
		
		if(dictionary != null)
			nvps.add(new BasicNameValuePair(Params.dictionary.name(), dictionary));
		
		nvps.add(new BasicNameValuePair("src", "sdk-java-1.0"));//internal management
		
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new TextalyticsClientException(500, "Problem encoding HTTP POST method parameters.\n", e);
		}
	}
}
