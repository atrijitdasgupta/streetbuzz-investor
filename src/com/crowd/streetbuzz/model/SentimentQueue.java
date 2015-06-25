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
/*
 * CREATE TABLE SB_SENTIMENT_QUEUE(
ID BIGINT not null auto_increment,
CARDID BIGINT,
TYPE VARCHAR(50),
INBLOB LONGBLOB,
OUTBLOB LONGBLOB,
STATUS BIGINT,
PRIMARY KEY (ID)) ENGINE=InnoDB;
 */
@Entity
@Table(name = "SB_SENTIMENT_QUEUE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class SentimentQueue {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TYPE")
	private String type;

	@Column(name = "CARDID")
	private Long cardid;

	@Column(name = "INBLOB")
	private byte[] inblob;

	@Column(name = "OUTBLOB")
	private byte[] outblob;
	
	@Column(name = "STATUS")
	private Long status;

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

	public byte[] getInblob() {
		return inblob;
	}

	public void setInblob(byte[] inblob) {
		this.inblob = inblob;
	}

	public byte[] getOutblob() {
		return outblob;
	}

	public void setOutblob(byte[] outblob) {
		this.outblob = outblob;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}
