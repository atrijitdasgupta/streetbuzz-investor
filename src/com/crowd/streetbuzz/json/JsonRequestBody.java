/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonRequestBody {
private String afterid;
private String perpage;
private String channel;
private String beforeid;
//query for search
private String q;

public String getAfterid() {
	return afterid;
}
public void setAfterid(String afterid) {
	this.afterid = afterid;
}
public String getBeforeid() {
	return beforeid;
}
public void setBeforeid(String beforeid) {
	this.beforeid = beforeid;
}
public String getChannel() {
	return channel;
}
public void setChannel(String channel) {
	this.channel = channel;
}
public String getPerpage() {
	return perpage;
}
public void setPerpage(String perpage) {
	this.perpage = perpage;
}
public String getQ() {
	return q;
}
public void setQ(String q) {
	this.q = q;
}


}
