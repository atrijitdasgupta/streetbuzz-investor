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
@Table(name="SB_CARD_INTEREST_DETECTED_MAP", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardInterestDetectedMap {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "INTERESTID")
	private Long interestid;
	
	/*@Column(name = "SUBINTERESTID")
	private Long subinterestid;*/
	
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

	public Long getInterestid() {
		return interestid;
	}

	public void setInterestid(Long interestid) {
		this.interestid = interestid;
	}

	public Long getProcessed() {
		return processed;
	}

	public void setProcessed(Long processed) {
		this.processed = processed;
	}

	/*public Long getSubinterestid() {
		return subinterestid;
	}

	public void setSubinterestid(Long subinterestid) {
		this.subinterestid = subinterestid;
	}*/
}
