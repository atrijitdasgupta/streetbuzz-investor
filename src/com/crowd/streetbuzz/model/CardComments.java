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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author Atrijit
 *
 */
/*
 * CREATE TABLE SB_CARD_COMMENTS(
ID BIGINT not null auto_increment,
CARDUNIQUEID VARCHAR(200),
CARDID BIGINT,
TYPE VARCHAR(1),
COMMENT VARCHAR(1000),
COMMENTER VARCHAR(200),
SBCOMMENTERID BIGINT,
EXTCOMMENTID VARCHAR(200),
EXTCOMMENTSOURCE VARCHAR(100),
COMMENTDATE TIMESTAMP,
PRIMARY KEY (ID)) ENGINE=InnoDB;
 */
@Entity
@Table(name="SB_CARD_COMMENTS", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class CardComments {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDUNIQUEID")
	private String carduniqueid;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "COMMENT")
	private String comment;
	
	@Column(name = "COMMENTER")
	private String commenter;
	
	@Column(name = "SBCOMMENTERID")
	private Long sbcommenterid;
	
	@Column(name = "EXTCOMMENTID")
	private String extcommenterid;
	
	@Column(name = "EXTCOMMENTSOURCE")
	private String extcommentersource;
	
	@Column(name = "COMMENTUNIQUEID")
	private String commentuniqueid;
	
	@Transient
	private Long userrating;
	
	//Added on 30.03.2015
	@Column(name = "LINK")
	private String link;
	
	@Column(name = "LINKPREVIEWURL")
	private String linkpreviewurl;
	
//	Added on 30.03.2015
	@Column(name = "MEDIAID")
	private String mediaid;
	
//	Added on 30.03.2015
	@Column(name = "RATING")
	private String rating;
	
//	Added on 09.05.2015
	@Column(name = "AVATAR")
	private String avatar;
	

	@Column(name = "COMMENTDATE")
	private Date commentdate;
	
	@Transient
	private String mediaurl;
	
	@Transient
	private String mediavideourl;
	
	@Transient 
	private String previewtext;
	
	@Transient 
	private boolean isowner;
	
	@Transient 
	private boolean isupvoted;
	
	@Transient 
	private boolean isdownvoted;
	
	@Transient 
	private Long upvotecount;
	
	@Transient 
	private Long downvotecount;
	
	@Transient
	private boolean isliked;
	
	@Transient
	private Long likecount;

	public boolean isIsliked() {
		return isliked;
	}

	public void setIsliked(boolean isliked) {
		this.isliked = isliked;
	}

	public Long getLikecount() {
		return likecount;
	}

	public void setLikecount(Long likecount) {
		this.likecount = likecount;
	}

	public String getPreviewtext() {
		return previewtext;
	}

	public void setPreviewtext(String previewtext) {
		this.previewtext = previewtext;
	}

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public String getCarduniqueid() {
		return carduniqueid;
	}

	public void setCarduniqueid(String carduniqueid) {
		this.carduniqueid = carduniqueid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCommentdate() {
		return commentdate;
	}

	public void setCommentdate(Date commentdate) {
		this.commentdate = commentdate;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	public String getExtcommenterid() {
		return extcommenterid;
	}

	public void setExtcommenterid(String extcommenterid) {
		this.extcommenterid = extcommenterid;
	}

	public String getExtcommentersource() {
		return extcommentersource;
	}

	public void setExtcommentersource(String extcommentersource) {
		this.extcommentersource = extcommentersource;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSbcommenterid() {
		return sbcommenterid;
	}

	public void setSbcommenterid(Long sbcommenterid) {
		this.sbcommenterid = sbcommenterid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getUserrating() {
		return userrating;
	}

	public void setUserrating(Long userrating) {
		this.userrating = userrating;
	}

	public String getCommentuniqueid() {
		return commentuniqueid;
	}

	public void setCommentuniqueid(String commentuniqueid) {
		this.commentuniqueid = commentuniqueid;
	}


	public String getLinkpreviewurl() {
		return linkpreviewurl;
	}

	public void setLinkpreviewurl(String linkpreviewurl) {
		this.linkpreviewurl = linkpreviewurl;
	}

	public String getMediavideourl() {
		return mediavideourl;
	}

	public void setMediavideourl(String mediavideourl) {
		this.mediavideourl = mediavideourl;
	}

	public boolean isIsowner() {
		return isowner;
	}

	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}

	public Long getDownvotecount() {
		return downvotecount;
	}

	public void setDownvotecount(Long downvotecount) {
		this.downvotecount = downvotecount;
	}

	public boolean isIsdownvoted() {
		return isdownvoted;
	}

	public void setIsdownvoted(boolean isdownvoted) {
		this.isdownvoted = isdownvoted;
	}

	public boolean isIsupvoted() {
		return isupvoted;
	}

	public void setIsupvoted(boolean isupvoted) {
		this.isupvoted = isupvoted;
	}

	public Long getUpvotecount() {
		return upvotecount;
	}

	public void setUpvotecount(Long upvotecount) {
		this.upvotecount = upvotecount;
	}

}
