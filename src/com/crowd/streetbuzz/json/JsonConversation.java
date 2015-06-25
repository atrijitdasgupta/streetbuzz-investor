/**
 * 
 */
package com.crowd.streetbuzz.json;

import java.io.Serializable;

/**
 * @author Atrijit
 *
 */
public class JsonConversation implements Serializable{
	static final long serialVersionUID = 1000000003L;
	private String conversationtopic;
	private String link;
	private String mediaid;
	private String mediatype;
	private String []socialshareon;
	private String interesttag;
	private String moreinteresttag;
	private String rating;
	private String additionalviews;
	private String latitude;
	private String longitude;
	private String interestid;
	public String getInterestid() {
		return interestid;
	}
	public void setInterestid(String interestid) {
		this.interestid = interestid;
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
	public String getAdditionalviews() {
		return additionalviews;
	}
	public void setAdditionalviews(String additionalviews) {
		this.additionalviews = additionalviews;
	}
	public String getConversationtopic() {
		return conversationtopic;
	}
	public void setConversationtopic(String conversationtopic) {
		this.conversationtopic = conversationtopic;
	}
	public String getInteresttag() {
		return interesttag;
	}
	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
	}
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
	public String getMediatype() {
		return mediatype;
	}
	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}
	public String getMoreinteresttag() {
		return moreinteresttag;
	}
	public void setMoreinteresttag(String moreinteresttag) {
		this.moreinteresttag = moreinteresttag;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String[] getSocialshareon() {
		return socialshareon;
	}
	public void setSocialshareon(String[] socialshareon) {
		this.socialshareon = socialshareon;
	}
	
	
	
}
