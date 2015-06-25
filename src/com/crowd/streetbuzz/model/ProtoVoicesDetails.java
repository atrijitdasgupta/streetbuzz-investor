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
@Table(name="SB_VOICES_DETAILSPROTO", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class ProtoVoicesDetails {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "VOICESID")
	private Long voicesid;
	
	@Column(name = "VOICEDATE")
	private Date voicedate;
	
	@Transient
	private int daysago;
	
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
	
	@Column(name = "AVATAR")
	private String avatar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
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

	public int getDaysago() {
		return daysago;
	}

	public void setDaysago(int daysago) {
		this.daysago = daysago;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
