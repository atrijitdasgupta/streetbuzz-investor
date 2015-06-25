/**
 * TextalyticsClientException.java
 * 
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */

package es.daedalus.textalytics.sempub;

/**
 * TextalyticsClientException
 * 
 * Encapsulates textalytics client errors.
 *  
 * @author Textalytics
 * @version 1.0 02/2014
 */
public class TextalyticsClientException extends Exception {

	private static final long serialVersionUID = 9177218654699062279L;
	
	private int code;
	private String message;
	
	public TextalyticsClientException(int code, String message, Throwable cause) {
		super(Integer.toString(code) + " " +  message, cause);
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
