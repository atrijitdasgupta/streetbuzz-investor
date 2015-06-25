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
@Table(name="SB_USER_CATEGORY_MAP", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class UserCategoryMap {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "CATEGORYID")
	private Long categoryid;
	
	@Column(name = "SUBCATEGORYID")
	private Long subcategoryid;
	
	@Column(name = "CATEGORY")
	private String category;
	
	@Column(name = "REPUTATIONSCORE")
	private Long reputationscore;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Long categoryid) {
		this.categoryid = categoryid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getReputationscore() {
		return reputationscore;
	}

	public void setReputationscore(Long reputationscore) {
		this.reputationscore = reputationscore;
	}

	public Long getSubcategoryid() {
		return subcategoryid;
	}

	public void setSubcategoryid(Long subcategoryid) {
		this.subcategoryid = subcategoryid;
	}
}
