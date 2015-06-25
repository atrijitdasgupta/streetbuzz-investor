/**
 * 
 */
package com.crowd.streetbuzz.model;

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
@Table(name="SB_INTEREST_SEARCH_TERMS", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class InterestSearchTerms {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "INTERESTID")
	private Long interestid;
	
	@Column(name = "PARENTINTERESTID")
	private Long parentinterestid;
	
	@Column(name = "BASESEARCHTERM")
	private String name;
	
	@Column(name = "STATUS")
	private String status;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentinterestid() {
		return parentinterestid;
	}

	public void setParentinterestid(Long parentinterestid) {
		this.parentinterestid = parentinterestid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
