/**
 * 
 */
package com.crowd.streetbuzzalgo.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Atrijit
 * 
 */
public class SearchVO implements Serializable{
	private String searchterm;

	private String url;

	// Facebook/Twitter/Mouthshut etc
	private String mode;

	private String text;

	private String commenter;

	private Date commentdate;

	private String location;

	private int sentimentscore;

	private String sentiment;

	private boolean searchwithlocation;

	private String thumbnail;
	
	private String profilepic;
	
	private String id;
	
	private String commenterid;
	
	private String positivephrase;
	
	private String negativephrase;
	
	private String neutralphrase;
	
	private String fbgroupid;
	
	private List crawlerList;

	public List getCrawlerList() {
		return crawlerList;
	}

	public void setCrawlerList(List crawlerList) {
		this.crawlerList = crawlerList;
	}

	public String getFbgroupid() {
		return fbgroupid;
	}

	public void setFbgroupid(String fbgroupid) {
		this.fbgroupid = fbgroupid;
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

	public String getCommenterid() {
		return commenterid;
	}

	public void setCommenterid(String commenterid) {
		this.commenterid = commenterid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public boolean isSearchwithlocation() {
		return searchwithlocation;
	}

	public void setSearchwithlocation(boolean searchwithlocation) {
		this.searchwithlocation = searchwithlocation;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public int getSentimentscore() {
		return sentimentscore;
	}

	public void setSentimentscore(int sentimentscore) {
		this.sentimentscore = sentimentscore;
	}

	public Date getCommentdate() {
		return commentdate;
	}

	public void setCommentdate(Date commentdate) {
		this.commentdate = commentdate;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSearchterm() {
		return searchterm;
	}

	public void setSearchterm(String searchterm) {
		this.searchterm = searchterm;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
}
