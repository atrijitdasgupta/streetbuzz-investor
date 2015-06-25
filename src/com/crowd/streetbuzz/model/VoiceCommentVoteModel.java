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
@Table(name="SB_VOICE_COMMENT_VOTE_MODEL", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class VoiceCommentVoteModel {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "COMMENTID")
	private Long commentid;
	
	@Column(name = "VOTE")
	private Integer vote;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "VOTEDATETIME")
	private Date votedatetime;

	public Long getCommentid() {
		return commentid;
	}

	public void setCommentid(Long commentid) {
		this.commentid = commentid;
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

	public Date getVotedatetime() {
		return votedatetime;
	}

	public void setVotedatetime(Date votedatetime) {
		this.votedatetime = votedatetime;
	}

}
