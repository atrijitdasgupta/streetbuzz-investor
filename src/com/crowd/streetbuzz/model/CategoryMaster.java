/**
 * 
 */
package com.crowd.streetbuzz.model;

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
@Table(name="SB_CATEGORY_MASTER", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CategoryMaster {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CATEGORYNAME")
	private String categoryname;
	
	@Column(name = "PARENTID")
	private Long parentid;
	
	@Column(name = "ICONCOLOUR")
	private String iconcolour;
	
	@Transient
	private Long priority;
	
	@Transient
	private List subcategorylist;

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIconcolour() {
		return iconcolour;
	}

	public void setIconcolour(String iconcolour) {
		this.iconcolour = iconcolour;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public List getSubcategorylist() {
		return subcategorylist;
	}

	public void setSubcategorylist(List subcategorylist) {
		this.subcategorylist = subcategorylist;
	}
	
	
}
