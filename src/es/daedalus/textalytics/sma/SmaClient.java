/**
 * SmaClient.java
 * 
 * @version 1.0
 * @author zdepablo
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sma;


import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import es.daedalus.textalytics.sma.domain.Document;
import es.daedalus.textalytics.sma.domain.Response;
import es.daedalus.textalytics.sma.domain.Result;

/**
 * SmaClient - wrapper around the textalytics.com SMA API 
 * 
 * @author zdepablo
 *
 */
public class SmaClient {

	private final static String HTTPS = "https";
	
	
	// POST param names 
	public enum Params {
		key, 
		input, 
		output, 
		fields,
		doc
	}

	// Allowed formats for input (in) and output (out)
	public enum Format {
		xml, json
	}

	
	// Types of analysis provided by the service 
	public enum AnalysisField {
		sentiment,
		categorization, 
		entities,
		concepts,
		timeExpressions,
		moneyExpressions,
		uris,
		phoneExpressions
	}
	
	private HttpClient httpclient;
	private ResponseHandler<Response> responseHandler;

	// POST param values
	private String host = "textalytics.com";
	private String path  = "/api/media/1.0/analyze";
	private String key;
    private String fields = null; // API default - produce all results 
    final private Format input = Format.json; // This client currently uses supports JSON
    final private Format output = Format.json; // This client currently supports JSON 
    
	
	public SmaClient(String key) {
		
		Properties prop = new Properties();

		this.key = key;

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
			
			// PROTOCOL is not used, only https implementation
			this.host = prop.getProperty("textalytics.host","textalytics.com");
			this.path = prop.getProperty("textalytics.service.endpoint","/api/media/1.0/analyze");
			
			System.out.println(host + path);

			KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
			truststore.load(null, null);

			SSLSocketFactory socketFactory = new SmaSSLSocketFactory(truststore);

			Scheme sch = new Scheme(HTTPS, socketFactory, 443);
			this.httpclient = new DefaultHttpClient(params);

			this.httpclient.getConnectionManager().getSchemeRegistry().register(sch);

		} catch (Exception e) {
			this.httpclient = new DefaultHttpClient(params);			
		}

		this.responseHandler = new SmaResponseHandler();
		
		

	}
	
	/**
	 * @return a pipe separated lists of the fields that are included in the analsysis, Default is to use all. 
	 */
	public String getFields() {
		if (fields == null) return fieldsValue(new TreeSet<SmaClient.AnalysisField>(Arrays.asList(AnalysisField.values())));
 		return fields;
	}

	/**
	 * Select the analysis to perform 
	 * 
	 * @param fields a pipe separated list of fields 
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}

	/**
	 * Select the analysis to perform
	 * 
	 * @param fields a set of fields 
	 */
	public void setFields(Set<AnalysisField> fields) {
		String s = fieldsValue(fields);
		setFields(s);
	}


	/**
	 * Analyzes a document with textalytics SMA
	 *  
	 * @param document 
	 * @return a result object if the process succeed and config is OK. 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Result analyze(Document document) throws ClientProtocolException, IOException {
		Response response = null;
		
		HttpPost post = new HttpPost(HTTPS + "://" + host + path);
		setPostParams(post, document);
		// if status not OK throws exception
		response = httpclient.execute(post, responseHandler);

		return response.getResult();
	}

	/**
	 * Analyzed a string of text 
	 * 	  
	 * @param text
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Result analyze(String text) throws ClientProtocolException, IOException {
		Document document = new Document("0", text);
		return analyze(document);
	}

		
	// Transform a set of fields into a value for the parameter fields separated by the pipe character
	private String fieldsValue(Set<AnalysisField> fields) {
		if (fields.isEmpty()) return null; 
		
		StringBuffer buffer = new StringBuffer();
		Iterator<AnalysisField> it = fields.iterator();
		buffer.append(it.next().toString());
		while (it.hasNext()) {
			buffer.append('|').append(it.next());
		}
		
		return buffer.toString();
	}


	private void setPostParams(HttpPost post, Document document) throws IOException {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair(Params.key.name(), this.key));
		nvps.add(new BasicNameValuePair(Params.input.name(), this.input.name()));
		nvps.add(new BasicNameValuePair(Params.output.name(), this.output.name()));
		nvps.add(new BasicNameValuePair("src", "sdk-java-1.0"));
		
		if (fields != null && !fields.isEmpty())
		nvps.add(new BasicNameValuePair(Params.fields.name(), this.fields));
				
		String s = document.toJsonString();
		nvps.add(new BasicNameValuePair(Params.doc.name(), s ));
		
		post.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (httpclient != null)
			httpclient.getConnectionManager().shutdown();
	}
}
