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
@Table(name="SB_CARD_SHARE", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardShare {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "SHAREDATE")
	private Date sharedate;

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSharedate() {
		return sharedate;
	}

	public void setSharedate(Date sharedate) {
		this.sharedate = sharedate;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
}
