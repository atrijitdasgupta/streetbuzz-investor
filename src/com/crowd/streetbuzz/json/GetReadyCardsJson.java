/**
 * 
 */
package com.crowd.streetbuzz.json;

/**
 * @author Atrijit
 *
 */
public class GetReadyCardsJson {
private String interestid;
private String perpage;
/*
 * all
 * bookmark
 * friends
 * 
 */
private String listtype;
//Optioal - if present, the card is for that person, otherwise for self
private String friendid;
private String beforeid;
private String afterid;

private String query;

public String getQuery() {
	return query;
}
public void setQuery(String query) {
	this.query = query;
}

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
public String getFriendid() {
	return friendid;
}
public void setFriendid(String friendid) {
	this.friendid = friendid;
}
public String getInterestid() {
	return interestid;
}
public void setInterestid(String interestid) {
	this.interestid = interestid;
}
public String getListtype() {
	return listtype;
}
public void setListtype(String listtype) {
	this.listtype = listtype;
}
public String getPerpage() {
	return perpage;
}
public void setPerpage(String perpage) {
	this.perpage = perpage;
}

}
