/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 * 
 */
public class JsonInterestProfileRequest implements Serializable {
	static final long serialVersionUID = 1000000004L;

	private String conversationtopic;

	public String getConversationtopic() {
		return conversationtopic;
	}

	public void setConversationtopic(String conversationtopic) {
		this.conversationtopic = conversationtopic;
	}
}
