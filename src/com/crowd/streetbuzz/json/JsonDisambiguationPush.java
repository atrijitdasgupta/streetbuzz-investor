/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.util.List;

/**
 * @author Atrijit
 *
 */
public class JsonDisambiguationPush {
private String type;
private Long id;
private List interestids;
private String topic;

public String getTopic() {
	return topic;
}
public void setTopic(String topic) {
	this.topic = topic;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public List getInterestids() {
	return interestids;
}
public void setInterestids(List interestids) {
	this.interestids = interestids;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

}
