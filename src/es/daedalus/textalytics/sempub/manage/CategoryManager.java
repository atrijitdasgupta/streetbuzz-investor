/**
 * CategoryManager.java
 *
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.manage;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import es.daedalus.textalytics.sempub.SempubClient.Format;
import es.daedalus.textalytics.sempub.SempubClient.Type;
import es.daedalus.textalytics.sempub.Response;
import es.daedalus.textalytics.sempub.TextalyticsClientException;
import es.daedalus.textalytics.sempub.domain.Category;
import es.daedalus.textalytics.sempub.utils.Conf;

/**
 * Helper class for making requests corresponding to category objects operations
 * in the User Resources Management service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class CategoryManager {

	private final static String MODEL_LIST = "/model_list/";
	private final static String CATEGORY_LIST = "/category_list";
	
	private String key;
	private Format input;
	private Format output;
	private String url;
	private HttpClient httpclient;
	private ResponseHandler<Response> responseHandler;
	private String protocol;
	
	public CategoryManager(String key, Format input, Format output, 
			String url, HttpClient httpclient,
			ResponseHandler<Response> responseHandler, String protocol) {
		super();
		this.key = key;
		this.input = input;
		this.output = output;
		this.url = url;
		this.httpclient = httpclient;
		this.responseHandler = responseHandler;
		this.protocol = protocol;
	}

	public List<Category> getList(String modelName, String query) throws ClientProtocolException, IOException {
		Response response = null;
		
		HttpGet get = new HttpGet(protocol + "://" + url + MODEL_LIST + modelName + CATEGORY_LIST + "?" + Conf.setGetParams(key, input, output, query, null));
		response = httpclient.execute(get, responseHandler);
		
		return response.getCategories();
	}
	
	public Category read(String id, String modelName) throws ClientProtocolException, IOException {
		Response response = null;
		
		HttpGet get = new HttpGet(protocol + "://" + url + MODEL_LIST + modelName + CATEGORY_LIST+ "/" + id + "?" + Conf.setGetParams(key, input, output));
		response = httpclient.execute(get, responseHandler);
		
		return response.getCategory();
	}
	
	public String create(Category category, String modelName) throws TextalyticsClientException, ClientProtocolException, IOException {
		Response response = null;
		
		HttpPost post = new HttpPost(protocol + "://" + url + MODEL_LIST + modelName + CATEGORY_LIST);
		Conf.setPostParams(post, category, Type.category, key, input, output);
		response = httpclient.execute(post, responseHandler);
				
		return response.getId();
	}
	
	public String update(Category category, String modelName) throws TextalyticsClientException, ClientProtocolException, IOException {
		Response response = null;
		
		HttpPut put = new HttpPut(protocol + "://" + url + MODEL_LIST + modelName + CATEGORY_LIST + "/" + category.getObjectIdentifier());
		Conf.setPutParams(put, category, Type.category, key, input, output);
		response = httpclient.execute(put, responseHandler);
				
		return response.getId();
	}
	
	public String delete(String id, String modelName) throws ClientProtocolException, IOException {
		Response response = null;
		
		HttpDelete delete = new HttpDelete(protocol + "://" + url + MODEL_LIST + modelName + CATEGORY_LIST + "/" + id + "?" + Conf.setDeleteParams(key, input, output));
		response = httpclient.execute(delete, responseHandler);
		
		return response.getId();
	}
}
