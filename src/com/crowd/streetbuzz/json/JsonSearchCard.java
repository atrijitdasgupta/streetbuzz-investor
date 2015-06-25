/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonSearchCard implements Serializable{
	static final long serialVersionUID = 1000000004L;
	private String priority;
	private String id;
	private String username;
	private String userid;
	private String usericonimage;
	private String searchtopic;
	private String interesttag;
	private String voicescount;
	private String commentscount;
	private String newvoicescount;
	private String newcommentscount;
	private String positivevoices;
	private String negativevoices;
	private String neutralvoices;
	private String timestamp;
	
	public String getNegativevoices() {
		return negativevoices;
	}
	public void setNegativevoices(String negativevoices) {
		this.negativevoices = negativevoices;
	}
	public String getNeutralvoices() {
		return neutralvoices;
	}
	public void setNeutralvoices(String neutralvoices) {
		this.neutralvoices = neutralvoices;
	}
	public String getPositivevoices() {
		return positivevoices;
	}
	public void setPositivevoices(String positivevoices) {
		this.positivevoices = positivevoices;
	}
	
	public String getSearchtopic() {
		return searchtopic;
	}
	public void setSearchtopic(String searchtopic) {
		this.searchtopic = searchtopic;
	}
	public String getCommentscount() {
		return commentscount;
	}
	public void setCommentscount(String commentscount) {
		this.commentscount = commentscount;
	}
	public String getInteresttag() {
		return interesttag;
	}
	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
	}
	public String getNewcommentscount() {
		return newcommentscount;
	}
	public void setNewcommentscount(String newcommentscount) {
		this.newcommentscount = newcommentscount;
	}
	public String getNewvoicescount() {
		return newvoicescount;
	}
	public void setNewvoicescount(String newvoicescount) {
		this.newvoicescount = newvoicescount;
	}
	public String getUsericonimage() {
		return usericonimage;
	}
	public void setUsericonimage(String usericonimage) {
		this.usericonimage = usericonimage;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getVoicescount() {
		return voicescount;
	}
	public void setVoicescount(String voicescount) {
		this.voicescount = voicescount;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
