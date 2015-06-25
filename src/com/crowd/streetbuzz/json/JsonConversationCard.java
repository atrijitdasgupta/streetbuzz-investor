/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonConversationCard implements Serializable{
	static final long serialVersionUID = 1000000005L;
	private String priority;
	private String id;
	private String username;
	private String userid;
	private String usericonimage;
	private String interesttag;
	private String rating;
	private String additionalviews;
	private String photoid;
	private String videoid;
	//keeping the next 4 just in case
	private String voicescount;
	private String commentscount;
	private String newvoicescount;
	private String newcommentscount;
	//
	private String facebookcount;
	private String twittercount;
	private String googlepluscount;
	private String streetbuzzcount;
	private String blogcount;
	private String webcount;
	
	private String newfacebookcount;
	private String newtwittercount;
	private String newgooglepluscount;
	private String newstreetbuzzcount;
	private String newblogcount;
	private String newwebcount;
	
	private String timestamp;
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAdditionalviews() {
		return additionalviews;
	}
	public void setAdditionalviews(String additionalviews) {
		this.additionalviews = additionalviews;
	}
	public String getBlogcount() {
		return blogcount;
	}
	public void setBlogcount(String blogcount) {
		this.blogcount = blogcount;
	}
	public String getCommentscount() {
		return commentscount;
	}
	public void setCommentscount(String commentscount) {
		this.commentscount = commentscount;
	}
	public String getFacebookcount() {
		return facebookcount;
	}
	public void setFacebookcount(String facebookcount) {
		this.facebookcount = facebookcount;
	}
	public String getGooglepluscount() {
		return googlepluscount;
	}
	public void setGooglepluscount(String googlepluscount) {
		this.googlepluscount = googlepluscount;
	}
	public String getInteresttag() {
		return interesttag;
	}
	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
	}
	public String getNewblogcount() {
		return newblogcount;
	}
	public void setNewblogcount(String newblogcount) {
		this.newblogcount = newblogcount;
	}
	public String getNewcommentscount() {
		return newcommentscount;
	}
	public void setNewcommentscount(String newcommentscount) {
		this.newcommentscount = newcommentscount;
	}
	public String getNewfacebookcount() {
		return newfacebookcount;
	}
	public void setNewfacebookcount(String newfacebookcount) {
		this.newfacebookcount = newfacebookcount;
	}
	public String getNewgooglepluscount() {
		return newgooglepluscount;
	}
	public void setNewgooglepluscount(String newgooglepluscount) {
		this.newgooglepluscount = newgooglepluscount;
	}
	public String getNewstreetbuzzcount() {
		return newstreetbuzzcount;
	}
	public void setNewstreetbuzzcount(String newstreetbuzzcount) {
		this.newstreetbuzzcount = newstreetbuzzcount;
	}
	public String getNewtwittercount() {
		return newtwittercount;
	}
	public void setNewtwittercount(String newtwittercount) {
		this.newtwittercount = newtwittercount;
	}
	public String getNewvoicescount() {
		return newvoicescount;
	}
	public void setNewvoicescount(String newvoicescount) {
		this.newvoicescount = newvoicescount;
	}
	public String getNewwebcount() {
		return newwebcount;
	}
	public void setNewwebcount(String newwebcount) {
		this.newwebcount = newwebcount;
	}
	public String getPhotoid() {
		return photoid;
	}
	public void setPhotoid(String photoid) {
		this.photoid = photoid;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getStreetbuzzcount() {
		return streetbuzzcount;
	}
	public void setStreetbuzzcount(String streetbuzzcount) {
		this.streetbuzzcount = streetbuzzcount;
	}
	public String getTwittercount() {
		return twittercount;
	}
	public void setTwittercount(String twittercount) {
		this.twittercount = twittercount;
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
	public String getVideoid() {
		return videoid;
	}
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}
	public String getVoicescount() {
		return voicescount;
	}
	public void setVoicescount(String voicescount) {
		this.voicescount = voicescount;
	}
	public String getWebcount() {
		return webcount;
	}
	public void setWebcount(String webcount) {
		this.webcount = webcount;
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
}
