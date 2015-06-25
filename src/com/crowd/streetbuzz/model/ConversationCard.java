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
@Table(name = "SB_CONVERSATION_CARD", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID" }) })
public class ConversationCard {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "UNIQUEID")
	private String uniqueid;

	@Column(name = "CONVERSATIONTOPIC")
	private String topic;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "LONGITUDE")
	private String longitude;

	@Column(name = "USERID")
	private String userid;

	// if "Y" action needed
	// if "N" no action needed
	@Column(name = "ACTION")
	private String action;

	@Column(name = "ACTIONTYPE")
	private String actiontype;

	@Column(name = "SOCIALNETWORK")
	private String socialnetwork;

	@Column(name = "CARDID")
	private String cardid;

	@Column(name = "CARDTYPE")
	private String cardtype;

	@Column(name = "VOICEID")
	private String voiceid;

	@Column(name = "SOURCE")
	private String source;

	@Column(name = "LASTUPDATE")
	private String lastupdate;

	@Column(name = "AFTERID")
	private String afterid;

	@Column(name = "LINK")
	private String link;

	@Column(name = "MEDIAID")
	private String mediaid;

	@Column(name = "RATING")
	private String rating;

	@Column(name = "ADDDITIONALVIEWS")
	private String additionalviews;

	@Column(name = "SOCIALSHAREON")
	private String socialshareon;

	@Column(name = "INTERESTTAG")
	private String interesttag;

	@Column(name = "MOREINTERESTTAG")
	private String moreinteresttag;

	@Column(name = "VOICESCOUNT")
	private Long voicescount;

	@Column(name = "COMMENTSCOUNT")
	private Long commentscount;

	@Column(name = "NEWVOICESCOUNT")
	private Long newvoicescount;

	@Column(name = "NEWCOMMENTSCOUNT")
	private Long newcommentscount;

	@Column(name = "POSITIVEVOICES")
	private Long positivevoices;

	@Column(name = "NEGATIVEVOICES")
	private Long negativevoices;

	@Column(name = "NEUTRALVOICES")
	private Long neutralvoices;

	@Column(name = "CREATIONDATE")
	private Date creationdate;
	
	@Column(name = "COMPLETIONDATE")
	private Date completiondate;

	@Column(name = "UPDATEDATE")
	private Date updatedate;

	@Column(name = "AUTHOR")
	private String author;

	@Column(name = "AVATAR")
	private String avatar;

	@Column(name = "CARDCITY")
	private String cardcity;

	@Column(name = "CARDCOUNTRY")
	private String cardcountry;

	@Column(name = "CITY")
	private String city;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "IMAGEURL")
	private String imageurl;

	@Column(name = "ISSYSTEMCARD")
	private String issystemcard;

	@Column(name = "INTERESTID")
	private Long interestid;
	
	@Column(name = "SUBINTERESTID")
	private Long subinterestid;
	
	@Column(name = "BARCHARTURL")
	private String barcharturl;
	
	@Column(name = "LINEMAPURL")
	private String linemapurl;
	
	@Column(name = "WORDCLOUDURL")
	private String wordcloudurl;
	
	@Column(name = "REVIEWCOUNT")
	private Long reviewcount;
	
	@Column(name = "FORUMCOUNT")
	private Long forumcount;
	
	@Column(name = "BLOGCOUNT")
	private Long blogcount;
	
	@Column(name = "FACEBOOKCOUNT")
	private Long facebookcount;
	
	@Column(name = "TWEETCOUNT")
	private Long tweetcount;
	
	@Column(name = "ARTICLECOUNT")
	private Long articlecount;
	
	@Column(name = "VIDEOCOUNT")
	private Long videocount;
	
	@Column(name = "IMAGECOUNT")
	private Long imagecount;
	
	@Transient
	private String shareurl;
	//
	@Transient
	private boolean isowner;
//
	@Transient
	private List userinterest;
	//
	@Transient
	private Long distributionsize;
	//
	@Transient
	private List channel;
	//
	@Transient
	private boolean systemcard;
	//
	@Transient 
	private String username;
	//
	@Transient 
	private Long userId;
	//
	@Transient
	private Long userrating;
	
	@Transient
	private boolean isupvoted;
	
	@Transient
	private boolean isdownvoted;
	
	@Transient
	private boolean isbookmarked;
	
	@Transient
	private Long upvotecount;
	
	@Transient
	private Long downvotecount;
	
	@Transient
	private Long isduplicate;
	
	@Transient
	private String barchartshareurl;
	
	@Transient
	private String linemapshareurl;
	
	@Transient
	private String wordcloudshareurl;
	
	@Transient
	private Long sharecount;
	
	
	//

	public Long getSharecount() {
		return sharecount;
	}

	public void setSharecount(Long sharecount) {
		this.sharecount = sharecount;
	}

	public Long getDownvotecount() {
		return downvotecount;
	}

	public void setDownvotecount(Long downvotecount) {
		this.downvotecount = downvotecount;
	}

	public Long getUpvotecount() {
		return upvotecount;
	}

	public void setUpvotecount(Long upvotecount) {
		this.upvotecount = upvotecount;
	}

	public Long getUserrating() {
		return userrating;
	}

	public void setUserrating(Long userrating) {
		this.userrating = userrating;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isSystemcard() {
		return systemcard;
	}

	public void setSystemcard(boolean systemcard) {
		this.systemcard = systemcard;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getAdditionalviews() {
		return additionalviews;
	}

	public void setAdditionalviews(String additionalviews) {
		this.additionalviews = additionalviews;
	}

	public String getAfterid() {
		return afterid;
	}

	public void setAfterid(String afterid) {
		this.afterid = afterid;
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

	public String getCardcity() {
		return cardcity;
	}

	public void setCardcity(String cardcity) {
		this.cardcity = cardcity;
	}

	public String getCardcountry() {
		return cardcountry;
	}

	public void setCardcountry(String cardcountry) {
		this.cardcountry = cardcountry;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getCommentscount() {
		return commentscount;
	}

	public void setCommentscount(Long commentscount) {
		this.commentscount = commentscount;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

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

	public String getInteresttag() {
		return interesttag;
	}

	public void setInteresttag(String interesttag) {
		this.interesttag = interesttag;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getMoreinteresttag() {
		return moreinteresttag;
	}

	public void setMoreinteresttag(String moreinteresttag) {
		this.moreinteresttag = moreinteresttag;
	}

	public Long getNegativevoices() {
		return negativevoices;
	}

	public void setNegativevoices(Long negativevoices) {
		this.negativevoices = negativevoices;
	}

	public Long getNeutralvoices() {
		return neutralvoices;
	}

	public void setNeutralvoices(Long neutralvoices) {
		this.neutralvoices = neutralvoices;
	}

	public Long getNewcommentscount() {
		return newcommentscount;
	}

	public void setNewcommentscount(Long newcommentscount) {
		this.newcommentscount = newcommentscount;
	}

	public Long getNewvoicescount() {
		return newvoicescount;
	}

	public void setNewvoicescount(Long newvoicescount) {
		this.newvoicescount = newvoicescount;
	}

	public Long getPositivevoices() {
		return positivevoices;
	}

	public void setPositivevoices(Long positivevoices) {
		this.positivevoices = positivevoices;
	}

	public String getSocialshareon() {
		return socialshareon;
	}

	public void setSocialshareon(String socialshareon) {
		this.socialshareon = socialshareon;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getSocialnetwork() {
		return socialnetwork;
	}

	public void setSocialnetwork(String socialnetwork) {
		this.socialnetwork = socialnetwork;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getVoiceid() {
		return voiceid;
	}

	public void setVoiceid(String voiceid) {
		this.voiceid = voiceid;
	}

	public Long getVoicescount() {
		return voicescount;
	}

	public void setVoicescount(Long voicescount) {
		this.voicescount = voicescount;
	}

	public Long getInterestid() {
		return interestid;
	}

	public void setInterestid(Long interestid) {
		this.interestid = interestid;
	}

	public String getIssystemcard() {
		return issystemcard;
	}

	public void setIssystemcard(String issystemcard) {
		this.issystemcard = issystemcard;
	}

	public List getChannel() {
		return channel;
	}

	public void setChannel(List channel) {
		this.channel = channel;
	}

	public Long getDistributionsize() {
		return distributionsize;
	}

	public void setDistributionsize(Long distributionsize) {
		this.distributionsize = distributionsize;
	}

	public boolean isIsowner() {
		return isowner;
	}

	public void setIsowner(boolean isowner) {
		this.isowner = isowner;
	}

	public List getUserinterest() {
		return userinterest;
	}

	public void setUserinterest(List userinterest) {
		this.userinterest = userinterest;
	}

	public boolean isIsbookmarked() {
		return isbookmarked;
	}

	public void setIsbookmarked(boolean isbookmarked) {
		this.isbookmarked = isbookmarked;
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

	public Date getCompletiondate() {
		return completiondate;
	}

	public void setCompletiondate(Date completiondate) {
		this.completiondate = completiondate;
	}

	public Long getSubinterestid() {
		return subinterestid;
	}

	public void setSubinterestid(Long subinterestid) {
		this.subinterestid = subinterestid;
	}

	public String getShareurl() {
		return shareurl;
	}

	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}

	public Long getIsduplicate() {
		return isduplicate;
	}

	public void setIsduplicate(Long isduplicate) {
		this.isduplicate = isduplicate;
	}

	public String getBarcharturl() {
		return barcharturl;
	}

	public void setBarcharturl(String barcharturl) {
		this.barcharturl = barcharturl;
	}

	public String getLinemapurl() {
		return linemapurl;
	}

	public void setLinemapurl(String linemapurl) {
		this.linemapurl = linemapurl;
	}

	public String getWordcloudurl() {
		return wordcloudurl;
	}

	public void setWordcloudurl(String wordcloudurl) {
		this.wordcloudurl = wordcloudurl;
	}

	public Long getArticlecount() {
		return articlecount;
	}

	public void setArticlecount(Long articlecount) {
		this.articlecount = articlecount;
	}

	public Long getBlogcount() {
		return blogcount;
	}

	public void setBlogcount(Long blogcount) {
		this.blogcount = blogcount;
	}

	public Long getFacebookcount() {
		return facebookcount;
	}

	public void setFacebookcount(Long facebookcount) {
		this.facebookcount = facebookcount;
	}

	public Long getForumcount() {
		return forumcount;
	}

	public void setForumcount(Long forumcount) {
		this.forumcount = forumcount;
	}

	public Long getImagecount() {
		return imagecount;
	}

	public void setImagecount(Long imagecount) {
		this.imagecount = imagecount;
	}

	public Long getReviewcount() {
		return reviewcount;
	}

	public void setReviewcount(Long reviewcount) {
		this.reviewcount = reviewcount;
	}

	public Long getTweetcount() {
		return tweetcount;
	}

	public void setTweetcount(Long tweetcount) {
		this.tweetcount = tweetcount;
	}

	public Long getVideocount() {
		return videocount;
	}

	public void setVideocount(Long videocount) {
		this.videocount = videocount;
	}

	public String getBarchartshareurl() {
		return barchartshareurl;
	}

	public void setBarchartshareurl(String barchartshareurl) {
		this.barchartshareurl = barchartshareurl;
	}

	public String getLinemapshareurl() {
		return linemapshareurl;
	}

	public void setLinemapshareurl(String linemapshareurl) {
		this.linemapshareurl = linemapshareurl;
	}

	public String getWordcloudshareurl() {
		return wordcloudshareurl;
	}

	public void setWordcloudshareurl(String wordcloudshareurl) {
		this.wordcloudshareurl = wordcloudshareurl;
	}



}
