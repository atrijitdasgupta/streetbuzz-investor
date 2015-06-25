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
@Table(name="SB_INTEREST_REFERENCE_MODEL", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class InterestReferenceModel {
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
	
	@Column(name = "SEARCHURL")
	private String searchurl;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "BODY")
	private String body;
	
	@Column(name = "KEYWORDS")
	private String keywords;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public String getSearchurl() {
		return searchurl;
	}

	public void setSearchurl(String searchurl) {
		this.searchurl = searchurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
