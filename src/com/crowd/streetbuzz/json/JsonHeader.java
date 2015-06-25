/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonHeader implements Serializable{
private String userid;
/*private String city;
private String country;*/
private String latitude;
private String longitude;
private String socialnetwork;
private String processid;
//private String serverid;
private String cardid;
private String cardtype;
private String voiceid;
private String source;
private String lastupdate;
private String carduniqueid;
//private String beforeid;
private String afterid;
private String height;
private String width;
static final long serialVersionUID = 1000000001L;


public String getLastupdate() {
	return lastupdate;
}
public void setLastupdate(String lastupdate) {
	this.lastupdate = lastupdate;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getCardid() {
	return cardid;
}
public void setCardid(String cardid) {
	this.cardid = cardid;
}
/*public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}*/
public String getProcessid() {
	return processid;
}
public void setProcessid(String processid) {
	this.processid = processid;
}
/*public String getServerid() {
	return serverid;
}
public void setServerid(String serverid) {
	this.serverid = serverid;
}*/
public String getSocialnetwork() {
	return socialnetwork;
}
public void setSocialnetwork(String socialnetwork) {
	this.socialnetwork = socialnetwork;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getVoiceid() {
	return voiceid;
}
public void setVoiceid(String voiceid) {
	this.voiceid = voiceid;
}
public String getCardtype() {
	return cardtype;
}
public void setCardtype(String cardtype) {
	this.cardtype = cardtype;
}
public String getAfterid() {
	return afterid;
}
public void setAfterid(String afterid) {
	this.afterid = afterid;
}
public String getLatitude() {
	return latitude;
}
public void setLatitude(String latitude) {
	this.latitude = latitude;
}
public String getLongitude() {
	return longitude;
}
public void setLongitude(String longitude) {
	this.longitude = longitude;
}
public String getCarduniqueid() {
	return carduniqueid;
}
public void setCarduniqueid(String carduniqueid) {
	this.carduniqueid = carduniqueid;
}
public String getHeight() {
	return height;
}
public void setHeight(String height) {
	this.height = height;
}
public String getWidth() {
	return width;
}
public void setWidth(String width) {
	this.width = width;
}

}
