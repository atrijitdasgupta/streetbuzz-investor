/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonPostMessage {
//private String userid;
private String touserid;
private String message;
private String replytomessageid;
private String link;
private String mediaid;

public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public String getMediaid() {
	return mediaid;
}
public void setMediaid(String mediaid) {
	this.mediaid = mediaid;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getReplytomessageid() {
	return replytomessageid;
}
public void setReplytomessageid(String replytomessageid) {
	this.replytomessageid = replytomessageid;
}
public String getTouserid() {
	return touserid;
}
public void setTouserid(String touserid) {
	this.touserid = touserid;
}
/*public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}*/

}
