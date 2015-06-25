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
/*
 * CREATE TABLE SB_USER_ACTIVITIES( ID BIGINT not null auto_increment, ENTITYID
 * BIGINT, USERID BIGINT, ACTIONDATETIME datetime, ACTIONTYPE VARCHAR(100),
 * ACTIONDETAILS VARCHAR(500), ACTIONAREA VARCHAR(100), POINTS BIGINT, PRIMARY
 * KEY (ID)) ENGINE=InnoDB;
 */
@Entity
@Table(name = "SB_USER_ACTIVITIES", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class UserActivities {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	// Entity id can point to cardid, voiceid, userid, messages etc etc based on
	// actiontype.
	@Column(name = "ENTITYID")
	private Long entityid;

	@Column(name = "ENTITYUSERID")
	private Long entityuserid;

	@Column(name = "USERID")
	private Long userid;

	@Column(name = "ACTIONDATETIME")
	private Date actiondatetime;

	@Column(name = "ACTIONTYPE")
	private String actiontype;

	@Column(name = "ACTIONDETAILS")
	private String actiondetails;

	@Column(name = "ACTIONAREA")
	private String actionarea;

	@Column(name = "POINTS")
	private Long points;

	public String getActionarea() {
		return actionarea;
	}

	public void setActionarea(String actionarea) {
		this.actionarea = actionarea;
	}

	public Date getActiondatetime() {
		return actiondatetime;
	}

	public void setActiondatetime(Date actiondatetime) {
		this.actiondatetime = actiondatetime;
	}

	public String getActiondetails() {
		return actiondetails;
	}

	public void setActiondetails(String actiondetails) {
		this.actiondetails = actiondetails;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public Long getEntityid() {
		return entityid;
	}

	public void setEntityid(Long entityid) {
		this.entityid = entityid;
	}

	public Long getEntityuserid() {
		return entityuserid;
	}

	public void setEntityuserid(Long entityuserid) {
		this.entityuserid = entityuserid;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
