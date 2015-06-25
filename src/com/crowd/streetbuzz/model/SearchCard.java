/**
 * 
 */
package com.crowd.streetbuzz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Atrijit
 * 
 */

@Entity
@Table(name = "SB_SEARCH_CARD", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class SearchCard {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "UNIQUEID")
	private String uniqueid;

	@Column(name = "SEARCHTOPIC")
	private String topic;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "LONGITUDE")
	private String longitude;

	@Column(name = "INTERESTTAG")
	private String interesttag;

	@Column(name = "MOREINTERESTTAG")
	private String moreinteresttag;

	@Column(name = "POSTONSOCIAL")
	private String postonsocial;

	@Column(name = "FACEBOOK")
	private String facebook;

	@Column(name = "TWITTER")
	private String twitter;

	@Column(name = "GOOGLEPLUS")
	private String googleplus;

	@Column(name = "POSTANONYMOUS")
	private String postanonymous;

	@Column(name = "VOICESCOUNT")
	private Long voicescount;

	@Column(name = "COMMENTSCOUNT")
	private Long commentscount;

	@Column(name = "NEWVOICESCOUNT")
	private Long newvoicescount;

	@Column(name = "NEWCOMMENTSCOUNT")
	private Long newcommentscount;

	@Column(name = "POSITIVEVOICES")
	private Long positivevoices;

	@Column(name = "NEGATIVEVOICES")
	private Long negativevoices;

	@Column(name = "NEUTRALVOICES")
	private Long neutralvoices;

	@Column(name = "ADDDITIONALVIEWS")
	private String additionalviews;
	
	@Column(name = "USERID")
	private String userid;
	
	//if "Y" action needed
	//if "N" no action needed
	//if "U" under processing
	@Column(name = "ACTION")
	private String action;
	
	@Column(name = "ACTIONTYPE")
	private String actiontype;
	
	@Column(name = "SOCIALNETWORK")
	private String socialnetwork;
	
	@Column(name = "CARDID")
	private String cardid;
	
	@Column(name = "CARDTYPE")
	private String cardtype;
	
	@Column(name = "VOICEID")
	private String voiceid;
	
	@Column(name = "SOURCE")
	private String source;
	
	@Column(name = "LASTUPDATE")
	private String lastupdate;

	@Column(name = "AFTERID")
	private String afterid;
	
	@Column(name = "CREATIONDATE")
	private Date creationdate;
	
	@Column(name = "UPDATEDATE")
	private Date updatedate;
	
	@Column(name = "AUTHOR")
	private String author;
	
	@Column(name = "AVATAR")
	private String avatar;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "CARDCITY")
	private String cardcity;
	
	@Column(name = "CARDCOUNTRY")
	private String cardcountry;
	
	@Column(name = "IMAGEURL")
	private String imageurl;
	
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCardcity() {
		return cardcity;
	}

	public void setCardcity(String cardcity) {
		this.cardcity = cardcity;
	}

	public String getCardcountry() {
		return cardcountry;
	}

	public void setCardcountry(String cardcountry) {
		this.cardcountry = cardcountry;
	}

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

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	
	public Long getCommentscount() {
		return commentscount;
	}

	public void setCommentscount(Long commentscount) {
		this.commentscount = commentscount;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInteresttag() {
		return interesttag;
	}

	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
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

	public Long getNegativevoices() {
		return negativevoices;
	}

	public void setNegativevoices(Long negativevoices) {
		this.negativevoices = negativevoices;
	}

	public Long getNeutralvoices() {
		return neutralvoices;
	}

	public void setNeutralvoices(Long neutralvoices) {
		this.neutralvoices = neutralvoices;
	}

	public Long getNewcommentscount() {
		return newcommentscount;
	}

	public void setNewcommentscount(Long newcommentscount) {
		this.newcommentscount = newcommentscount;
	}

	public Long getNewvoicescount() {
		return newvoicescount;
	}

	public void setNewvoicescount(Long newvoicescount) {
		this.newvoicescount = newvoicescount;
	}

	public Long getPositivevoices() {
		return positivevoices;
	}

	public void setPositivevoices(Long positivevoices) {
		this.positivevoices = positivevoices;
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

	
	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public Long getVoicescount() {
		return voicescount;
	}

	public void setVoicescount(Long voicescount) {
		this.voicescount = voicescount;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAfterid() {
		return afterid;
	}

	public void setAfterid(String afterid) {
		this.afterid = afterid;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getSocialnetwork() {
		return socialnetwork;
	}

	public void setSocialnetwork(String socialnetwork) {
		this.socialnetwork = socialnetwork;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getAdditionalviews() {
		return additionalviews;
	}

	public void setAdditionalviews(String additionalviews) {
		this.additionalviews = additionalviews;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

}
