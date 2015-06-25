/**
 * 
 */
package com.crowd.streetbuzzalgo.yahoosearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.log4j.Logger;

/**
 * @author Atrijit
 *
 */
public class StHTTPRequest {
	private static final Logger log = Logger.getLogger(StHTTPRequest.class);

    private String responseBody = "";
   
    private OAuthConsumer consumer = null;
	/**
	 * 
	 */
	public StHTTPRequest() {
		// TODO Auto-generated constructor stub
	}
	
	 public StHTTPRequest(OAuthConsumer consumer) {
	        this.consumer = consumer;
	    }
	  public HttpsURLConnection getConnection(String url) 
	    throws IOException,
	        OAuthMessageSignerException,
	        OAuthExpectationFailedException, 
	        OAuthCommunicationException
	    {
		  
		  try {
	            URL u = new URL(url);

	            HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
	             
	            if (consumer != null) {
	                try {
	                    log.info("Signing the oAuth consumer");
	                    consumer.sign(uc);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                } 
	                uc.connect();
	            }
	            return uc;
	        } catch (IOException e) {
	            log.error("Error signing the consumer", e);
	            throw e;
	        }
		  
	    }
	  
	  public int sendGetRequest(String url) 
	    throws IOException,
	    OAuthMessageSignerException,
	    OAuthExpectationFailedException, 
	    OAuthCommunicationException {
		  
		  int responseCode = 500;
	        try {
	            HttpsURLConnection uc = getConnection(url);

	            responseCode = uc.getResponseCode();
	            if(200 == responseCode || 401 == responseCode || 404 == responseCode){
	                BufferedReader rd = new BufferedReader(new InputStreamReader(responseCode==200?uc.getInputStream():uc.getErrorStream()));
	                StringBuffer sb = new StringBuffer();
	                String line;
	                while ((line = rd.readLine()) != null) {
	                    sb.append(line);
	                }
	                rd.close();
	                setResponseBody(sb.toString());
	            }
	        } catch (MalformedURLException ex) {
	            throw new IOException( url + " is not valid");
	        } catch (IOException ie) {
	            throw new IOException("IO Exception " + ie.getMessage());
	        }
	        return responseCode;
	  }
	  
	  /**
	     * Return the Response body
	     * @return String
	     */
	    public String getResponseBody() {
	        return responseBody;
	    }

	    /**
	     * Setter
	     * @param responseBody
	     */
	    public void setResponseBody(String responseBody) {
	        if (null != responseBody) {
	            this.responseBody = responseBody;
	        }
	    }
	    
	    /**
	     * Set the oAuth consumer
	     * @param consumer
	     */
	    public void setOAuthConsumer(OAuthConsumer consumer) {
	        this.consumer = consumer;
	    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
