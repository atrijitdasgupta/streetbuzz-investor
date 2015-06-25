/**
 * 
 */
package com.crowd.streetbuzz.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author Atrijit
 *
 */
@Entity
@Table(name="SB_USER", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class User {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "AVATAR")
	private String avatar;
	
	@Column(name = "LATITUDE")
	private String latitude;
	
	@Column(name = "LONGITUDE")
	private String longitude;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "FACEBOOK")
	private String facebook;
	
	@Column(name = "GOOGLEPLUS")
	private String googleplus;
	
	@Column(name = "TWITTER")
	private String twitter;
	
	@Column(name = "GOOGLEDEVICEID")
	private String googledeviceid;
	
	@Column(name = "APPLEDEVICEID")
	private String appledeviceid;
	
	@Column(name = "SOCIALUSERID")
	private String socialuserid;
	
	@Column(name = "PLACE")
	private String place;
	
	@Column(name = "RATING")
	private String rating;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "ACCESSTOKEN")
	private String accesstoken;
	
	//used for logging in
	@Column(name = "SOCIALNETWORK")
	private String socialnetwork;
	
	@Column(name = "JOINDATE")
	private Date joindate;
	
	@Transient
	private List userinterest;


	public List getUserinterest() {
		return userinterest;
	}

	public void setUserinterest(List userinterest) {
		this.userinterest = userinterest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Date getJoindate() {
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}

	
	public String getSocialnetwork() {
		return socialnetwork;
	}

	public void setSocialnetwork(String socialnetwork) {
		this.socialnetwork = socialnetwork;
	}

	

	public String getSocialuserid() {
		return socialuserid;
	}

	public void setSocialuserid(String socialuserid) {
		this.socialuserid = socialuserid;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getAppledeviceid() {
		return appledeviceid;
	}

	public void setAppledeviceid(String appledeviceid) {
		this.appledeviceid = appledeviceid;
	}

	public String getGoogledeviceid() {
		return googledeviceid;
	}

	public void setGoogledeviceid(String googledeviceid) {
		this.googledeviceid = googledeviceid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
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
}
