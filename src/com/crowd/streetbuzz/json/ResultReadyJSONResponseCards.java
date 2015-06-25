/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.util.List;

/**
 * @author Atrijit
 *
 */
public class ResultReadyJSONResponseCards {
	private String type;
	private List data;
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
