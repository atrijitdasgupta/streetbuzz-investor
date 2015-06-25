/**
 * SmaResponseHandler.java
 * 
 * @version 1.0
 * @author zdepablo
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sma;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.daedalus.textalytics.sma.domain.Response;
import es.daedalus.textalytics.sma.domain.Status;

/**
 * SmaResponseHandler
 * 
 * maps the response of textalytics API to POJOs in the domain package
 *  
 * @author zdepablo
 *
 */
public class SmaResponseHandler implements ResponseHandler<Response> {

	ObjectMapper mapper;
	
	public SmaResponseHandler() {
		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	

	/**
	 * Implements the handleResponse method for httpclient.
	 */
	public Response handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {

        	HttpEntity entity = response.getEntity();        	
        	if (entity != null) {
            	Response r = mapper.readValue(entity.getContent(), Response.class);
            	Status apiStatus = r.getStatus();
            	
                throw new TextalyticsException(
                		statusLine.getStatusCode(), 
                		statusLine.getReasonPhrase(), 
                		apiStatus.getCode(), 
                		apiStatus.getMessage(),
                		apiStatus.getMoreInfo());
        	} else {
                throw new TextalyticsException(statusLine.getStatusCode(), statusLine.getReasonPhrase());        		
        	}
        	
        	
        }

        HttpEntity entity = response.getEntity();
        
        return entity == null ? null : mapper.readValue(entity.getContent(), Response.class);
	}

}
