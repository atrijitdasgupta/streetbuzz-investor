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
@Entity
@Table(name="SB_VOICES_DETAILS", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class VoicesDetails {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VOICESID")
	private Long voicesid;
	
	@Column(name = "VOICEDATE")
	private Date voicedate;
	
	@Column(name = "SENTIMENTSCORE")
	private Long sentimentscore;
	
	@Column(name = "POSTTEXT")
	private String posttext;
	
	@Column(name = "POSTTEXTAUTHOR")
	private String posttextauthor;
	
	@Column(name = "POSTAUTHORID")
	private String postauthorid;
	
	@Column(name = "SENTIMENTRATING")
	private String sentimentrating;
	
	@Column(name = "POSITIVEPHRASE")
	private String positivephrase;
	
	@Column(name = "NEUTRALPHRASE")
	private String neutralphrase;
	
	@Column(name = "NEGATIVEPHRASE")
	private String negativephrase;
	
	@Column(name = "IMAGEURL")
	private String imageurl;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Transient
	private boolean sbuser;
	
	//Added as discussed with Pixel 6 on 13.05.2013 for compliance with card comments structure
	@Column(name = "COMMENT")
	private String comment;
	
	@Column(name = "COMMENTER")
	private String commenter;
	
	@Transient
	private String link;
	
	@Transient
	private String linkpreviewurl;
	
	@Transient
	private String mediaid;
	
	@Transient 
	private Date commentdate;
	
	@Transient 
	private String type;
	
	@Transient
	private String mediaurl;
	
	@Transient
	private String mediavideourl;
	
	@Transient
	private String rating;
	
	@Transient
	private String avatar;
	
	@Transient
	private String previewtext;
	
	@Transient
	private Long userrating;
	
	

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

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUserrating() {
		return userrating;
	}

	public void setUserrating(Long userrating) {
		this.userrating = userrating;
	}

	public boolean isSbuser() {
		return sbuser;
	}

	public void setSbuser(boolean sbuser) {
		this.sbuser = sbuser;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNegativephrase() {
		return negativephrase;
	}

	public void setNegativephrase(String negativephrase) {
		this.negativephrase = negativephrase;
	}

	public String getNeutralphrase() {
		return neutralphrase;
	}

	public void setNeutralphrase(String neutralphrase) {
		this.neutralphrase = neutralphrase;
	}

	public String getPositivephrase() {
		return positivephrase;
	}

	public void setPositivephrase(String positivephrase) {
		this.positivephrase = positivephrase;
	}

	public String getPostauthorid() {
		return postauthorid;
	}

	public void setPostauthorid(String postauthorid) {
		this.postauthorid = postauthorid;
	}

	public String getPosttext() {
		return posttext;
	}

	public void setPosttext(String posttext) {
		this.posttext = posttext;
	}

	public String getPosttextauthor() {
		return posttextauthor;
	}

	public void setPosttextauthor(String posttextauthor) {
		this.posttextauthor = posttextauthor;
	}

	public String getSentimentrating() {
		return sentimentrating;
	}

	public void setSentimentrating(String sentimentrating) {
		this.sentimentrating = sentimentrating;
	}

	public Long getSentimentscore() {
		return sentimentscore;
	}

	public void setSentimentscore(Long sentimentscore) {
		this.sentimentscore = sentimentscore;
	}

	public Date getVoicedate() {
		return voicedate;
	}

	public void setVoicedate(Date voicedate) {
		this.voicedate = voicedate;
	}

	public Long getVoicesid() {
		return voicesid;
	}

	public void setVoicesid(Long voicesid) {
		this.voicesid = voicesid;
	}

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public String getPreviewtext() {
		return previewtext;
	}

	public void setPreviewtext(String previewtext) {
		this.previewtext = previewtext;
	}

	
	
}
