/**
 * 
 */
package com.crowd.streetbuzzalgo.vimeo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Atrijit
 *
 */
public class VimeoResponse {
	private JSONObject json;
	private int statusCode;

	public VimeoResponse(JSONObject json, int statusCode) {
		this.json = json;
		this.statusCode = statusCode;
	}

	public JSONObject getJson() {
		return json;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String toString() {
		StringBuffer sbfr = new StringBuffer();
		try {
			sbfr.append("HTTP Status Code: \n").append(getStatusCode()).append("\nJson: \n").append(getJson().toString(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sbfr.toString();
	}
}
