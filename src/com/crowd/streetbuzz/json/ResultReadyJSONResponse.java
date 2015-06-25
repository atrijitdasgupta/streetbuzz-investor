/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.util.List;

/**
 * @author Atrijit
 *
 */
public class ResultReadyJSONResponse {
private String type;
private JSONdisambiguationtype data;

public JSONdisambiguationtype getData() {
	return data;
}
public void setData(JSONdisambiguationtype data) {
	this.data = data;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
}
