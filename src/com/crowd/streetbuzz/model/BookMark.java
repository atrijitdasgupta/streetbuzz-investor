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
@Table(name="SB_BOOK_MARK", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class BookMark {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ENTITYID")
	private Long entityid;
	
	@Column(name = "INTERESTID")
	private Long interestid;
	
	@Column(name = "ENTITYTYPE")
	private String entitytype;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "BOOKMARKDATE")
	private Date bookmarkdate;

	public Date getBookmarkdate() {
		return bookmarkdate;
	}

	public void setBookmarkdate(Date bookmarkdate) {
		this.bookmarkdate = bookmarkdate;
	}

	public Long getEntityid() {
		return entityid;
	}

	public void setEntityid(Long entityid) {
		this.entityid = entityid;
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

	public String getEntitytype() {
		return entitytype;
	}

	public void setEntitytype(String entitytype) {
		this.entitytype = entitytype;
	}

	public Long getInterestid() {
		return interestid;
	}

	public void setInterestid(Long interestid) {
		this.interestid = interestid;
	}
}
