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
@Table(name="SB_INTEREST_KEYWORDS", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class InterestKeywords {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "INTERESTID")
	private Long interestid;
	
	@Column(name = "PARENTINTERESTID")
	private Long parentinterestid;
	
	@Column(name = "KEYWORD")
	private String keyword;
	
	@Column(name = "SCORE")
	private Double score;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getParentinterestid() {
		return parentinterestid;
	}

	public void setParentinterestid(Long parentinterestid) {
		this.parentinterestid = parentinterestid;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
}
