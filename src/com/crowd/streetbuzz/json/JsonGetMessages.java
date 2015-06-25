/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class JsonGetMessages {
private String perpage;
private String beforeid;
private String afterid;

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
public String getPerpage() {
	return perpage;
}
public void setPerpage(String perpage) {
	this.perpage = perpage;
}


}
