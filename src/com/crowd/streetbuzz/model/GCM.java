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
@Table(name="SB_GCM", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class GCM {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USERID")
	private String userid;
	
	@Column(name = "SOCIALNETWORK")
	private String socialnetwork;
	
	@Column(name = "GOOGLEID")
	private String googleid;
	
	@Column(name = "APPLEID")
	private String appleid;
	
	@Column(name = "UPDATEDATE")
	private Date updatedate;

	public String getAppleid() {
		return appleid;
	}

	public void setAppleid(String appleid) {
		this.appleid = appleid;
	}

	public String getGoogleid() {
		return googleid;
	}

	public void setGoogleid(String googleid) {
		this.googleid = googleid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSocialnetwork() {
		return socialnetwork;
	}

	public void setSocialnetwork(String socialnetwork) {
		this.socialnetwork = socialnetwork;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
	
	
	
	
}
