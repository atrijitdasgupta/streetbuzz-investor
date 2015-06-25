/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonSearch implements Serializable{

	private String city;
	private String searchtopic;
	private String country;
	private String latitude;
	private String longitude;
	private String interesttag;
	private String moreinteresttag;
	private String postonsocial;
	private String facebook;
	private String twitter;
	private String googleplus;
	private String postanonymous;
	private String type;
	static final long serialVersionUID = 1000000002L;
	
	public String getCity() {
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
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getGoogleplus() {
		return googleplus;
	}
	public void setGoogleplus(String googleplus) {
		this.googleplus = googleplus;
	}
	public String getInteresttag() {
		return interesttag;
	}
	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
	}
	public String getPostanonymous() {
		return postanonymous;
	}
	public void setPostanonymous(String postanonymous) {
		this.postanonymous = postanonymous;
	}
	public String getPostonsocial() {
		return postonsocial;
	}
	public void setPostonsocial(String postonsocial) {
		this.postonsocial = postonsocial;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSearchtopic() {
		return searchtopic;
	}
	public void setSearchtopic(String searchtopic) {
		this.searchtopic = searchtopic;
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
	public String getMoreinteresttag() {
		return moreinteresttag;
	}
	public void setMoreinteresttag(String moreinteresttag) {
		this.moreinteresttag = moreinteresttag;
	}
	
	

}
