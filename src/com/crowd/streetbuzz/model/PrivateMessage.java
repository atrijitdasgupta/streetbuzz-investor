/**
 * 
 */
package com.crowd.streetbuzz.model;

import java.util.Date;
import java.util.List;

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
 * 
 * CREATE TABLE SB_PRIVATE_MESSAGES( ID BIGINT not null auto_increment,
 * REPLYTOMESSAGEID BIGINT, ISREPLY VARCHAR(2), TEXT VARCHAR(1000), AUTHORAVATAR
 * VARCHAR(200), MESSAGEBYID BIGINT, MESSAGEBY VARCHAR(200), MESSAGEAT
 * VARCHAR(200), MEDIAURL VARCHAR(400), ISOWNER VARCHAR(2), LINK VARCHAR(400),
 * MESSAGEDATE datetime, PRIMARY KEY (ID)) ENGINE=InnoDB;
 */

@Entity
@Table(name = "SB_PRIVATE_MESSAGES", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class PrivateMessage {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	//if a reply
	@Column(name = "REPLYTOMESSAGEID")
	private Long replytomessageid;

	@Column(name = "ISREPLY")
	private String isreply;

	@Column(name = "TEXT")
	private String text;

	@Column(name = "AUTHORAVATAR")
	private String authoravatar;

	@Column(name = "MESSAGEBYUSERID")
	private Long messagebyuserid;
	
	@Column(name = "MESSAGETOUSERID")
	private Long messagetouserid;

	@Column(name = "MESSAGEBY")
	private String messageby;
	
	@Column(name = "MESSAGETO")
	private String messageto;

	@Column(name = "MESSAGEAT")
	private Date messageat;

	@Column(name = "MEDIAURL")
	private String mediaurl;
	
	@Column(name = "MEDIAVIDEOURL")
	private String mediavideourl;
	
	@Column(name = "PREVIEWTEXT")
	private String previewtext;
	
	@Column(name = "MEDIAID")
	private String mediaid;

	@Column(name = "ISOWNER")
	private String isowner;

	@Column(name = "LINK")
	private String link;
	
	@Column(name = "LINKPREVIEWURL")
	private String linkpreviewurl;
	
	@Column(name = "UNIQUEID")
	private String uniqueid;
	
	@Transient
	private List replylist;
	
	@Transient
	private String userrating;

	public String getUserrating() {
		return userrating;
	}

	public void setUserrating(String userrating) {
		this.userrating = userrating;
	}

	public String getAuthoravatar() {
		return authoravatar;
	}

	public void setAuthoravatar(String authoravatar) {
		this.authoravatar = authoravatar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List getReplylist() {
		return replylist;
	}

	public void setReplylist(List replylist) {
		this.replylist = replylist;
	}

	public String getIsowner() {
		return isowner;
	}

	public void setIsowner(String isowner) {
		this.isowner = isowner;
	}

	public String getIsreply() {
		return isreply;
	}

	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMediaurl() {
		return mediaurl;
	}

	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}

	public String getMessageby() {
		return messageby;
	}

	public void setMessageby(String messageby) {
		this.messageby = messageby;
	}

	public Long getReplytomessageid() {
		return replytomessageid;
	}

	public void setReplytomessageid(Long replytomessageid) {
		this.replytomessageid = replytomessageid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}



	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public Date getMessageat() {
		return messageat;
	}

	public void setMessageat(Date messageat) {
		this.messageat = messageat;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public Long getMessagebyuserid() {
		return messagebyuserid;
	}

	public void setMessagebyuserid(Long messagebyuserid) {
		this.messagebyuserid = messagebyuserid;
	}

	public Long getMessagetouserid() {
		return messagetouserid;
	}

	public void setMessagetouserid(Long messagetouserid) {
		this.messagetouserid = messagetouserid;
	}

	public String getMessageto() {
		return messageto;
	}

	public void setMessageto(String messageto) {
		this.messageto = messageto;
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
