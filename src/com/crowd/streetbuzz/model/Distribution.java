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
@Table(name = "SB_DISTRIBUTION", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class Distribution {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SOURCEUSERID")
	private Long sourceuserid;
	
	@Column(name = "DESTINATIONUSERID")
	private Long destinationuserid;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "INTERESTID")
	private Long interestid;
	
	@Column(name = "DISTRIBUTIONDATE")
	private Date distributiondate;
	
	//"Y" - new
	//"N" - not new
	@Column(name = "NEWFLAG")
	private String newflag;
	
	@Column(name = "FLAG")
	private String flag;

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getDestinationuserid() {
		return destinationuserid;
	}

	public void setDestinationuserid(Long destinationuserid) {
		this.destinationuserid = destinationuserid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInterestid() {
		return interestid;
	}

	public void setInterestid(Long interestid) {
		this.interestid = interestid;
	}

	public Long getSourceuserid() {
		return sourceuserid;
	}

	public void setSourceuserid(Long sourceuserid) {
		this.sourceuserid = sourceuserid;
	}

	public Date getDistributiondate() {
		return distributiondate;
	}

	public void setDistributiondate(Date distributiondate) {
		this.distributiondate = distributiondate;
	}

	public String getNewflag() {
		return newflag;
	}

	public void setNewflag(String newflag) {
		this.newflag = newflag;
	}


	
	
	
}
