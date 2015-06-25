package com.crowd.streetbuzz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="SB_CARD_EXCLUSION", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardExclusion {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "PROCESSED")
	private Long processed;

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

	public Long getProcessed() {
		return processed;
	}

	public void setProcessed(Long processed) {
		this.processed = processed;
	}

	
	
	
}
