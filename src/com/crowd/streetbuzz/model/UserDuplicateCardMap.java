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
@Table(name="SB_USER_DUPLICATE_CARD_MAP", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class UserDuplicateCardMap {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "INTERESTID")
	private Long interestid;

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

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getInterestid() {
		return interestid;
	}

	public void setInterestid(Long interestid) {
		this.interestid = interestid;
	}
}
