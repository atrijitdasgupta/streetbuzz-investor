package com.crowd.streetbuzz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="SB_TAXONOMY", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class Taxonomy {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ROOTWORD")
	private String rootword;
	
	@Column(name = "LINKEDWORD")
	private String linkedword;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "SCORE")
	private Integer score;
	
	@Column(name = "CATEGORYID")
	private Long categoryid;
	
	@Column(name = "RUNNUM")
	private Integer run;
	
	@Column(name = "HASHVALUE")
	private String hash;
	
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

	public String getLinkedword() {
		return linkedword;
	}

	public void setLinkedword(String linkedword) {
		this.linkedword = linkedword;
	}

	public String getRootword() {
		return rootword;
	}

	public void setRootword(String rootword) {
		this.rootword = rootword;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRun() {
		return run;
	}

	public void setRun(Integer run) {
		this.run = run;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	
	
}
