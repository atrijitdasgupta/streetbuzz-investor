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
@Table(name="SB_VOICE_VOTE_MODEL", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class VoiceVoteModel {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VOICEID")
	private Long voiceid;
	
	@Column(name = "VOTE")
	private Integer vote;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "VOTEDATETIME")
	private Date votedatetime;

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

	public Long getVoiceid() {
		return voiceid;
	}

	public void setVoiceid(Long voiceid) {
		this.voiceid = voiceid;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Date getVotedatetime() {
		return votedatetime;
	}

	public void setVotedatetime(Date votedatetime) {
		this.votedatetime = votedatetime;
	}
}
