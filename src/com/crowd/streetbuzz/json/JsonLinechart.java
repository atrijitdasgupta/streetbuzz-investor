/**
 * 
 */
package com.crowd.streetbuzz.json;


/**
 * @author Atrijit
 *
 */
public class JsonLinechart {
	
private String sentiment;
private Long sentimentrating;
private String date;
private Long count;

public Long getCount() {
	return count;
}
public void setCount(Long count) {
	this.count = count;
}

public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getSentiment() {
	return sentiment;
}
public void setSentiment(String sentiment) {
	this.sentiment = sentiment;
}
public Long getSentimentrating() {
	return sentimentrating;
}
public void setSentimentrating(Long sentimentrating) {
	this.sentimentrating = sentimentrating;
}


}
