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
@Entity
@Table(name="SB_VOICES", uniqueConstraints={@UniqueConstraint(columnNames={"ID"})})
public class Voices {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CARDID")
	private Long cardid;
	
	@Column(name = "CARDUNIQUEID")
	private String carduniqueid;
	
	@Column(name = "CARDTYPE")
	private String cardtype;
	
	@Column(name = "USERID")
	private Long userid;
	
	@Column(name = "AUTHOR")
	private String author;
	
	@Column(name = "AVATAR")
	private String avatar;
	
	@Column(name = "POSTTEXT")
	private String posttext;
	
	@Column(name = "POSTAUTHORID")
	private String postauthorid;
	
	@Column(name = "POSTTEXTAUTHOR")
	private String posttextauthor;
	
	@Column(name = "POSTID")
	private String postid;
	
	@Column(name = "SOURCE")
	private String source;
	
	@Column(name = "VOICETYPE")
	private String voicetype;
	
	@Column(name = "SENTIMENTRATING")
	private String sentimentrating;
	
	@Column(name = "SENTIMENTSCORE")
	private Long sentimentscore;
	
	@Column(name = "POSITIVEPHRASE")
	private String positivephrase;
	
	@Column(name = "NEUTRALPHRASE")
	private String neutralphrase;
	
	@Column(name = "NEGATIVEPHRASE")
	private String negativephrase;
	
	@Column(name = "SOURCELINK")
	private String sourcelink;
	
	@Column(name = "UNIQUEVOICEID")
	private String uniquevoiceid;
	
	@Column(name = "FBGROUPID")
	private String fbgroupid;
	
	@Column(name = "VOICESDATE")
	private Date voicesdate;
	
	@Column(name = "LIKESCOUNT")
	private Long likescount;
	
	@Column(name = "THUMBSUPCOUNT")
	private Long thumbsupcount;
	
	@Column(name = "THUMBSDOWNCOUNT")
	private Long thumbsdowncount;
	
	@Column(name = "COMMENTSCOUNT")
	private Long commentscount;
	
	@Column(name = "EXTLIKESCOUNT")
	private Long extlikescount;
	
	@Column(name = "EXTCOMMENTSCOUNT")
	private Long extcommentscount;
	
	@Column(name = "EXTVIEWSCOUNT")
	private Long extviewscount;
	
	@Column(name = "THUMB")
	private String thumb;
	
	@Column(name = "CHANNEL")
	private String channel;
	
	@Column(name = "IMAGEURL")
	private String imageurl;
	
	@Transient
	private boolean cancomment;
	
	@Transient
	private boolean sbuser;
		
	@Transient
	private List comments;
	
	@Transient
	private Integer daysago;
	// as per Prasad's mail
	@Transient
	private Integer likecount;
	
//	@Transient
//	private Integer dislikecount;
	
//	@Transient
//	private boolean isdisliked;
	
	@Transient
	private boolean isliked;
	
	@Transient
	private String firstletter;
	
	@Transient
	private Long sharecount;

	public List getComments() {
		return comments;
	}

	public void setComments(List comments) {
		this.comments = comments;
	}

	public Long getCommentscount() {
		return commentscount;
	}

	public void setCommentscount(Long commentscount) {
		this.commentscount = commentscount;
	}

	public Long getExtcommentscount() {
		return extcommentscount;
	}

	public void setExtcommentscount(Long extcommentscount) {
		this.extcommentscount = extcommentscount;
	}

	public Long getExtlikescount() {
		return extlikescount;
	}

	public void setExtlikescount(Long extlikescount) {
		this.extlikescount = extlikescount;
	}

	public Long getExtviewscount() {
		return extviewscount;
	}

	public void setExtviewscount(Long extviewscount) {
		this.extviewscount = extviewscount;
	}

	public Long getLikescount() {
		return likescount;
	}

	public void setLikescount(Long likescount) {
		this.likescount = likescount;
	}

	public Long getThumbsdowncount() {
		return thumbsdowncount;
	}

	public void setThumbsdowncount(Long thumbsdowncount) {
		this.thumbsdowncount = thumbsdowncount;
	}

	public Long getThumbsupcount() {
		return thumbsupcount;
	}

	public void setThumbsupcount(Long thumbsupcount) {
		this.thumbsupcount = thumbsupcount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourcelink() {
		return sourcelink;
	}

	public void setSourcelink(String sourcelink) {
		this.sourcelink = sourcelink;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getVoicetype() {
		return voicetype;
	}

	public void setVoicetype(String voicetype) {
		this.voicetype = voicetype;
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

	public String getPositivephrase() {
		return positivephrase;
	}

	public void setPositivephrase(String positivephrase) {
		this.positivephrase = positivephrase;
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

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getUniquevoiceid() {
		return uniquevoiceid;
	}

	public void setUniquevoiceid(String uniquevoiceid) {
		this.uniquevoiceid = uniquevoiceid;
	}

	public Date getVoicesdate() {
		return voicesdate;
	}

	public void setVoicesdate(Date voicesdate) {
		this.voicesdate = voicesdate;
	}

	public String getFbgroupid() {
		return fbgroupid;
	}

	public void setFbgroupid(String fbgroupid) {
		this.fbgroupid = fbgroupid;
	}

	public String getCarduniqueid() {
		return carduniqueid;
	}

	public void setCarduniqueid(String carduniqueid) {
		this.carduniqueid = carduniqueid;
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

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public boolean getCancomment() {
		return cancomment;
	}

	public void setCancomment(boolean cancomment) {
		this.cancomment = cancomment;
	}

	public Integer getDaysago() {
		return daysago;
	}

	public void setDaysago(Integer daysago) {
		this.daysago = daysago;
	}

	public boolean isSbuser() {
		return sbuser;
	}

	public void setSbuser(boolean sbuser) {
		this.sbuser = sbuser;
	}

	public boolean isIsliked() {
		return isliked;
	}

	public void setIsliked(boolean isliked) {
		this.isliked = isliked;
	}

	public Integer getLikecount() {
		return likecount;
	}

	public void setLikecount(Integer likecount) {
		this.likecount = likecount;
	}

	public String getFirstletter() {
		return firstletter;
	}

	public void setFirstletter(String firstletter) {
		this.firstletter = firstletter;
	}

	public Long getSharecount() {
		return sharecount;
	}

	public void setSharecount(Long sharecount) {
		this.sharecount = sharecount;
	}

	
	
}
