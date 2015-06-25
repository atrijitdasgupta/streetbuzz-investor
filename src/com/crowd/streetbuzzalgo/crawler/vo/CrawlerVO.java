/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Atrijit
 *
 */
public class CrawlerVO implements Serializable{
private String URLParsed;
private String mode;
private String comment;
private String username;
private String timestamp;
private String location;
private int sentimentscore;
private String sentimentstr;
private String positivephrase;
private String negativephrase;
private String neutralphrase;
private String avatar;
private Date date;

public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public String getAvatar() {
	return avatar;
}
public void setAvatar(String avatar) {
	this.avatar = avatar;
}
public String getNegativephrase() {
	return negativephrase;
}
public void setNegativephrase(String negativephrase) {
	this.negativephrase = negativephrase;
}
public String getNeutralphrase() {
	return neutralphrase;
}
public void setNeutralphrase(String neutralphrase) {
	this.neutralphrase = neutralphrase;
}
public String getPositivephrase() {
	return positivephrase;
}
public void setPositivephrase(String positivephrase) {
	this.positivephrase = positivephrase;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}

public int getSentimentscore() {
	return sentimentscore;
}
public void setSentimentscore(int sentimentscore) {
	this.sentimentscore = sentimentscore;
}
public String getSentimentstr() {
	return sentimentstr;
}
public void setSentimentstr(String sentimentstr) {
	this.sentimentstr = sentimentstr;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
public String getURLParsed() {
	return URLParsed;
}
public void setURLParsed(String parsed) {
	URLParsed = parsed;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getMode() {
	return mode;
}
public void setMode(String mode) {
	this.mode = mode;
}
}
