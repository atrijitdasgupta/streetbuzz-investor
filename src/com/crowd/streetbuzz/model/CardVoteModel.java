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
/*
 * CREATE TABLE SB_CARD_VOTE_MODEL(
ID BIGINT not null auto_increment,
CARDID BIGINT,
USERID BIGINT,
VOTE BIGINT,
VOTEDATETIME datetime,
PRIMARY KEY (ID)) ENGINE=InnoDB;
 */
@Entity
@Table(name="SB_CARD_VOTE_MODEL", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardVoteModel {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "VOTE")
	private Integer vote;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "VOTEDATETIME")
	private Date votedatetime;

	public Date getVotedatetime() {
		return votedatetime;
	}

	public void setVotedatetime(Date votedatetime) {
		this.votedatetime = votedatetime;
	}

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

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
