/**
 * TextalyticsException.java
 * 
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub;

import java.io.IOException;

/**
 * TextalyticsException
 * 
 * Encapsulates textalytics API errors.
 *  
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class TextalyticsException extends IOException {

	private static final long serialVersionUID = -2203380451627761210L;

    private final int httpCode;
    private final String httpMessage;
    private final int statusCode;
    
    public TextalyticsException(int httpCode, String httpMessage, int statusCode, String statusMessage, String moreInfo) {
    	super(Integer.toString(statusCode) + " " +  statusMessage + " ( " + moreInfo + " )");
    	this.httpCode = httpCode;
    	this.httpMessage = httpMessage;
    	this.statusCode = statusCode;
	}

    
    public TextalyticsException(int httpCode, String httpMessage) {
    	super(Integer.toString(100) + " Unknown API error "  + " ( " + Integer.toString(httpCode) + " - " +  httpMessage + " )");
    	this.httpCode = httpCode;
    	this.httpMessage = httpMessage;
    	this.statusCode = 100;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public String getHttpMessage() {
		return httpMessage;
	}


	public int getStatusCode() {
		return statusCode;
	}

}
